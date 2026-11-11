package demo;

// This comment is ignored by the Lexer
@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        return "ok";   /* also ignored */
    }
}
