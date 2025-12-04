import { useState } from "react";
import { Screen } from "../App";
import { useRegiser } from "../hooks/useRegister";


export default function Register({ setScreen }: { setScreen: (s: Screen) => void }) {

    const { register } = useRegiser();
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

        </>
    )
}