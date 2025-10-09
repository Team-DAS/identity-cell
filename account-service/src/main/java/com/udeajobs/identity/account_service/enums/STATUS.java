package com.udeajobs.identity.account_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enumeración que define los posibles estados de un usuario en el sistema.
 *
 * Esta enumeración se utiliza para controlar el ciclo de vida de las cuentas
 * de usuario, desde el registro inicial hasta la activación completa.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public enum STATUS {

    /** Usuario registrado pero pendiente de verificación por email */
    PENDING_VERIFICATION,

    /** Usuario verificado y activo en el sistema */
    ACTIVE,

    /** Usuario desactivado o suspendido */
    INACTIVE;

    /**
     * Crea un STATUS a partir de un valor String, manejando la conversión de manera segura.
     *
     * @param value el valor string a convertir (no sensible a mayúsculas/minúsculas)
     * @return el STATUS correspondiente al valor proporcionado
     * @throws IllegalArgumentException si el valor no corresponde a ningún STATUS válido
     */
    @JsonCreator
    public static STATUS fromString(String value) {
        try {
            return STATUS.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + value);
        }
    }
}
