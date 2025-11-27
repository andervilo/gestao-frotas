package dev.andervilo.gestao_frotas.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * Value Object representing a Brazilian vehicle license plate.
 * Supports both old format (ABC-1234) and new Mercosul format (ABC1D23).
 */
@Getter
@EqualsAndHashCode
public class LicensePlate {
    
    private static final Pattern OLD_FORMAT = Pattern.compile("^[A-Z]{3}-?\\d{4}$");
    private static final Pattern MERCOSUL_FORMAT = Pattern.compile("^[A-Z]{3}\\d[A-Z]\\d{2}$");
    
    private final String value;
    
    public LicensePlate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }
        
        String normalized = value.toUpperCase().replace("-", "").trim();
        
        if (!isValid(normalized)) {
            throw new IllegalArgumentException("Invalid license plate format: " + value);
        }
        
        this.value = normalized;
    }
    
    private boolean isValid(String plate) {
        return OLD_FORMAT.matcher(plate).matches() || 
               MERCOSUL_FORMAT.matcher(plate).matches();
    }
    
    public String getFormatted() {
        if (value.length() == 7 && Character.isLetter(value.charAt(4))) {
            // Mercosul format: ABC1D23
            return value;
        } else {
            // Old format: ABC-1234
            return value.substring(0, 3) + "-" + value.substring(3);
        }
    }
    
    @Override
    public String toString() {
        return getFormatted();
    }
}
