package net.aholbrook.paseto.examples.custom_claim;

import net.aholbrook.paseto.meta.PasetoBuilders;
import net.aholbrook.paseto.service.Token;
import net.aholbrook.paseto.service.TokenService;
import org.apache.commons.codec.DecoderException;

import java.time.Duration;
import java.util.Objects;

public class Main {
    public static byte[] decode(String hex) {
        try {
            return org.apache.commons.codec.binary.Hex.decodeHex(hex);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    public static class CustomToken extends Token {
        private String custom;

        public String getCustom() {
            return custom;
        }

        public void setCustom(String custom) {
            this.custom = custom;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            CustomToken that = (CustomToken) o;
            return Objects.equals(custom, that.custom);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), custom);
        }
    }

    public static void main(String[] args) {
        byte[] key = decode("707172737475767778797a7b7c7d7e7f808182838485868788898a8b8c8d8e8f");
        TokenService<CustomToken> tokenService = PasetoBuilders.V2.localService(() -> key, CustomToken.class)
                .withDefaultValidityPeriod(Duration.ofDays(15).getSeconds())
                .build();

        CustomToken token = new CustomToken();
        token.setCustom("Whatever you'd like here.");
        token.setTokenId("example-id"); // A session key, user id, etc.

        String encoded = tokenService.encode(token);
        System.out.println(encoded);

        CustomToken decoded = tokenService.decode(encoded);
        System.out.println(decoded.getCustom());
    }
}
