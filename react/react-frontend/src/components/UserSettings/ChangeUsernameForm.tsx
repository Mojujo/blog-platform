import { useState } from "react";
import { useUserEdit } from "../../hooks/useUserEdit"

export const ChangeUsernameForm = () => {
    const { changeUsername, loading, error } = useUserEdit();
    const [username, setUsername] = useState("");
    const [success, setSuccess] = useState(false);

    const submit = async () => {
        const ok = await changeUsername(username);
        setSuccess(ok)
    };

    return (
        <>
            <div>
                <h3>Change Username</h3>
                <input
                    value={username}
                    onChange={e => setUsername(e.target.value)}
                    placeholder="New username"
                />
                <button onClick={submit} disabled={loading || !username}>Update</button>
                {success && <p>Username updated</p>}
                {error && <p>{error}</p>}
            </div>
        </>
    )
}