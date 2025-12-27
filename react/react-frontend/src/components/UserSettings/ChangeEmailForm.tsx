import { useState } from "react";
import { useUserEdit } from "../../hooks/useUserEdit"
import { Screen } from "../../App";

export const ChangeEmailForm = ({ setScreen }: { setScreen: (s: Screen) => void }) => {
    const { changeEmail, status, error } = useUserEdit(() => setScreen("login"));
    const [email, setEmail] = useState("");
    const [success, setSuccess] = useState(false);

    const submit = async () => {
        const ok = await changeEmail(email);
        setSuccess(ok);
    }

    return (
        <>
            <div>
                <h3>Change Email</h3>
                <input
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    placeholder="New Email"
                />
                <button onClick={submit} disabled={status === "loading" || !email}>Update</button>
                {success && <p>Email updated</p>}
                {error && <p>{error}</p>}
            </div>
        </>
    )
}