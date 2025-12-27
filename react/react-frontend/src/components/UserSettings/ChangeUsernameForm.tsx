import { useState } from "react";
import { useUserEdit } from "../../hooks/useUserEdit"
import { Screen } from "../../App";

export const ChangeUsernameForm = ({ setScreen }: { setScreen: (s: Screen) => void }) => {
    const { changeUsername, status, error } = useUserEdit(() => setScreen("login"));
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