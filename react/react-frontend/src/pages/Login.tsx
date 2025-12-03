import { useState } from "react"
import apiClient from "../api/apiClient"
import { getXsrfToken } from "../util/csrfUtil"

export default function Login() {

    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [message, setMessage] = useState("")

    const handleLogin = async () => {
        const xsrfToken = getXsrfToken();

        try {
            const response = await apiClient.post("/auth/login",
                { username, password },
                { headers: { "X-XSRF-TOKEN": xsrfToken || "" } }
            );

            setMessage(`Welcome ${response.data.username}`);

        } catch (err: any) {
            setMessage(err.response?.data.message || "Login Failed");
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