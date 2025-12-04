import { useState } from "react";
import { Screen } from "../App";
import { useRegister } from "../hooks/useRegister";
import styles from "./Register.module.css"


export default function Register({ setScreen }: { setScreen: (s: Screen) => void }) {

    const { register } = useRegister();
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");

    const handleRegister = async () => {

        const success = await register(username, email, password)

        if (success) {
            setScreen("login")
        } else {
            setMessage("Registration Failed")
        }
    };

    return (
        <>
            <div className={styles.registerContainer}>
                <h2>Register</h2>
                <input
                    id="username"
                    placeholder="Username"
                    value={username}
                    onChange={string => setUsername(string.target.value)}
                />
                <input
                    id="email"
                    placeholder="Email"
                    value={email}
                    onChange={string => setEmail(string.target.value)}
                />
                <input
                    id="password"
                    placeholder="Password"
                    value={password}
                    onChange={string => setPassword(string.target.value)}
                />

                <button onClick={handleRegister}>Register</button>
                {message && <p> {message} </p>}
            </div>
        </>
    )
}