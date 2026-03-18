package wing.tongtin.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wing.tongtin.demo.entity.KycEntity;
import wing.tongtin.demo.entity.UserEntity;
import wing.tongtin.demo.enumeration.KycDocumentType;
import wing.tongtin.demo.enumeration.KycStatus;
import wing.tongtin.demo.repository.KycRepository;
import wing.tongtin.demo.repository.UserRepository;
import wing.tongtin.demo.request.KycReviewRequest;
import wing.tongtin.demo.response.KycResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KycService {

    private final KycRepository kycRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public KycResponse submitKycDocument(String userId, MultipartFile file, KycDocumentType type) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if document type already exists for this user
        if (kycRepository.existsByUserAndType(user, type)) {
            throw new RuntimeException("KYC document of type " + type + " already submitted");
        }

        // Upload file to Cloudinary
        String fileUrl = cloudinaryService.uploadFile(file, "kyc/" + userId);

        KycEntity kyc = KycEntity.builder()
                .file(fileUrl)
                .type(type)
                .user(user)
                .status(KycStatus.PENDING)
                .build();

        KycEntity saved = kycRepository.save(kyc);
        return KycResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<KycResponse> getUserKycDocuments(String userId) {
        return kycRepository.findByUserId(userId).stream()
                .map(KycResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<KycResponse> getPendingKycDocuments() {
        return kycRepository.findByStatus(KycStatus.PENDING).stream()
                .map(KycResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<KycResponse> getAllKycDocuments() {
        return kycRepository.findAll().stream()
                .map(KycResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public KycResponse reviewKycDocument(String kycId, String adminId, KycReviewRequest request) {
        KycEntity kyc = kycRepository.findById(kycId)
                .orElseThrow(() -> new RuntimeException("KYC document not found"));

        if (kyc.getStatus() != KycStatus.PENDING) {
            throw new RuntimeException("KYC document has already been reviewed");
        }

        kyc.setStatus(request.getStatus());
        kyc.setReviewedBy(adminId);
        kyc.setReviewedAt(LocalDateTime.now());

        if (request.getStatus() == KycStatus.REJECTED && request.getRejectionReason() != null) {
            kyc.setRejectionReason(request.getRejectionReason());
        }

        KycEntity savedKyc = kycRepository.save(kyc);

        // Check if all required documents are approved
        if (request.getStatus() == KycStatus.APPROVED) {
            checkAndUpdateUserKycStatus(kyc.getUser());
        }

        return KycResponse.fromEntity(savedKyc);
    }

    private void checkAndUpdateUserKycStatus(UserEntity user) {
        List<KycEntity> userDocuments = kycRepository.findByUser(user);

        boolean hasIdFront = userDocuments.stream()
                .anyMatch(doc -> doc.getType() == KycDocumentType.ID_FRONT && doc.getStatus() == KycStatus.APPROVED);
        boolean hasIdBack = userDocuments.stream()
                .anyMatch(doc -> doc.getType() == KycDocumentType.ID_BACK && doc.getStatus() == KycStatus.APPROVED);
        boolean hasSelfie = userDocuments.stream()
                .anyMatch(doc -> doc.getType() == KycDocumentType.SELFIE && doc.getStatus() == KycStatus.APPROVED);

        // All three documents must be approved
        if (hasIdFront && hasIdBack && hasSelfie) {
            user.setKycVerified(true);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public KycResponse getKycDocument(String kycId) {
        KycEntity kyc = kycRepository.findById(kycId)
                .orElseThrow(() -> new RuntimeException("KYC document not found"));
        return KycResponse.fromEntity(kyc);
    }

    @Transactional
    public List<KycResponse> approveAllUserKyc(String userId, String adminId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<KycEntity> pendingDocuments = kycRepository.findByUser(user).stream()
                .filter(doc -> doc.getStatus() == KycStatus.PENDING)
                .collect(Collectors.toList());

        if (pendingDocuments.isEmpty()) {
            throw new RuntimeException("No pending KYC documents found for this user");
        }

        LocalDateTime now = LocalDateTime.now();
        for (KycEntity kyc : pendingDocuments) {
            kyc.setStatus(KycStatus.APPROVED);
            kyc.setReviewedBy(adminId);
            kyc.setReviewedAt(now);
            kycRepository.save(kyc);
        }

        // Set user as KYC verified
        user.setKycVerified(true);
        userRepository.save(user);

        return pendingDocuments.stream()
                .map(KycResponse::fromEntity)
                .collect(Collectors.toList());
    }
}