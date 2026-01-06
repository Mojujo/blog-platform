import { useState } from "react";
import { useUserEdit } from "../../hooks/useUserEdit"
import styles from "./UserModifyForm.module.css"

export const ChangeUsernameForm = () => {
    const { changeUsername, status, error } = useUserEdit();
    const [username, setUsername] = useState("");
    const [success, setSuccess] = useState(false);

    const submit = async () => {
        const ok = await changeUsername(username);
        setSuccess(ok);

        if (ok) {
            setUsername("");
        }
    };

    return (
        <>
            <div>
                <h3>Change Username</h3>
                <input
                    value={username}
                    name="username-edit"
                    onChange={e => setUsername(e.target.value)}
                    placeholder="New username"
                />
                <button onClick={submit} disabled={status === "loading" || !username}>Update</button>
                {success && <p>Username updated</p>}
                {error && <p>{error}</p>}
            </div>
        </>
    )
}