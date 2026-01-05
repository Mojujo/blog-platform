import { useState } from "react"
import { useAuth } from "../context/AuthContext"
import styles from "./Login.module.css"
import { useScreen } from "../context/ScreenContext";

export default function Login() {

    const { setScreen } = useScreen();
    const { login } = useAuth();
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [message, setMessage] = useState("")

    const handleLogin = async () => {
        const success = await login(username, password);

        if (success) {
            setMessage("Welcome");
            setScreen("home");
        } else {
            setMessage("Login failed");
        }
    };

    return (
        <>
            <div className={styles.loginContainer}>
                <h2>Login</h2>
                <input
                    id="username"
                    placeholder="Username"
                    value={username}
                    onChange={(string) => setUsername(string.target.value)}
                />
                <input
                    id="password"
                    placeholder="Password"
                    value={password}
                    onChange={(string) => setPassword(string.target.value)}
                />
                <button onClick={async () => {
                    await handleLogin();
                }}>Login</button>
                <p>{message}</p>
            </div>
            <div className={styles.forwardRegister}>
                <h3>No account?</h3>
                <button onClick={() => setScreen("register")}>Register</button>
            </div>
        </>
    )
}