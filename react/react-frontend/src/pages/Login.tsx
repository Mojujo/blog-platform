import { useState } from "react"
import { useAuth } from "../context/AuthContext"

export default function Login() {

    const { login } = useAuth();

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [message, setMessage] = useState("")

    const handleLogin = async () => {
        const success = await login(username, password);

        if (success) {
            setMessage("Welcome");
        } else {
            setMessage("Login failed");
        }
    };

    return (
        <>
            <div>
                <h2>Login</h2>
                <input
                    placeholder="Username"
                    value={username}
                    onChange={(string) => setUsername(string.target.value)}
                />
                <input
                    placeholder="Password"
                    value={password}
                    onChange={(string) => setPassword(string.target.value)}
                />
                <button onClick={handleLogin}>Login</button>
                <p>{message}</p>
            </div>
        </>
    )
}