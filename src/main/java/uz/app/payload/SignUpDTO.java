package uz.app.payload;

public record SignUpDTO(
        String firstName,
        String lastName,
        String username,
        String password,
        int age) {
}