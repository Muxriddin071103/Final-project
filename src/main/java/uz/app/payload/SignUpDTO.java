package uz.app.payload;

public record SignUpDTO(
        String username,
        String firstName,
        String lastName,
        String password,
        int age) {
}