/*
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class KmsTest {
    @Value("${aws.kms.keyId}")
    String KEY_ID="kms";

    @Test
    void encrypt() {
        final String plaintext = "암호화할문장";

        try {
            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            EncryptRequest request = new EncryptRequest();
            request.withKeyId(KEY_ID);
            request.withPlaintext(ByteBuffer.wrap(plaintext.getBytes(StandardCharsets.UTF_8)));
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);

            EncryptResult result = kmsClient.encrypt(request);
            ByteBuffer ciphertextBlob = result.getCiphertextBlob();

            System.out.println("ciphertextBlob: " + new String(Base64.encodeBase64(ciphertextBlob.array())));

        } catch (Exception e) {
            System.out.println("encrypt fail: " + e.getMessage());
        }
    }

    @Test
    void decrypt() {
        final String encriptedText = "kB01x8ngDmdQx4EHfGDx0eeEoJewPnKnNeif9n5TB9AUt93oQQCrywwZ8pPIdPtG09qN3oTXcOSCG5uGg0koSX7HoR1eG4NV+tfgmWnOMhQBY3Ku8WG7Ka5Oxp+S5BAttWI3SmtPG+KPrCGOq4YHbOrBEjmNyp8GqO/TV13P8e5tlstYwDrf025pAQcbTpJBn8IKhWdOT49bdXyHUqVx36fHQsJbtFTMDE6MUV64hlR9FBDDYkpQ//lJiakfQi578TWzZ042mWkvrQeMG+rWXFbUgHgJUMh4gBEknCJzlOz8kmELSypsWcE1nMhkjL+KTR6oRpOeJu0b3I/ZKljc4DY5UTvyYq6NyrdMUXt6YSXOi07Sv3loY/W0WfbpcL7EkRBtPQPLThr91cJlV1K4p5RTUyY6lncqQAsaEFQcarOOC2r6XCdXdyvm1TIb2ak0qMWxq5mXuDy0xWB/FKHdtxIaFEZj/B+0nxsmLmBmuR0p5gikjX8EsyWBRl4PDKGL0hvMqkSiDr0Do72vKssP16rHKy+4lZftuEYIcIIoC5s6hV1JdBtILL0dK5ag8GgdebbAbSWtP4m8s9SNfgLZCEdAT3Yuf3gUFsxEi+CPp2iOFmtgmdKRQ43VlI56Onbodn13VxKwwyRsxO2ybb0iCnJb5UBsnnjXQlEQGSbgia0=";

        try {
            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            DecryptRequest request = new DecryptRequest();
            request.withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(encriptedText)));
            request.withKeyId(KEY_ID);
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
            ByteBuffer plainText = kmsClient.decrypt(request).getPlaintext();

            System.out.println("plainText: " + new String(plainText.array()));
        } catch (Exception e) {
            System.out.println("decrypt fail: " + e.getMessage());
        }
    }
}

 */

