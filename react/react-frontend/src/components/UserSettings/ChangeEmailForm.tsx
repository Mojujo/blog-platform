import { useState } from "react";
import { useUserEdit } from "../../hooks/useUserEdit"

export const ChangeEmailForm = () => {
    const { changeEmail, status, error } = useUserEdit();
    const [email, setEmail] = useState("");
    const [success, setSuccess] = useState(false);

    const submit = async () => {
        const ok = await changeEmail(email);
        setSuccess(ok);

        if (ok) {
            setEmail("");
        }
    }

    return (
        <>
            <div>
                <h3>Change Email</h3>
                <input
                    value={email}
                    name="email-edit"
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