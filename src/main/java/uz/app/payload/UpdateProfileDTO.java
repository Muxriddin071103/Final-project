package uz.app.payload;

public record UpdateProfileDTO(
        String username,
        String firstName,
        String lastName,
        String oldPassword,
        String newPassword,
        int age) {
}