import { useState } from "react"
import apiClient from "../api/apiClient";

type Status = "idle" | "loading" | "success" | "error";

export const useUserEdit = () => {
    const [status, setStatus] = useState<Status>("idle");
    const [error, setError] = useState<string | null>(null);

    const patch = async (url: string, body: object) => {
        setStatus("loading")
        setError(null);

        try {
            await apiClient.patch(url, body);
            return true;
        } catch (err: any) {
            setStatus("error");
            setError(err?.response?.data?.detail ?? "Error updating profile");
        } finally {
            setStatus("success")
        }
    };

    return {
        status,
        error,
        changeUsername: (newUsername: string) =>
            patch("/user/username", { newUsername }),
        changeEmail: (newEmail: string) =>
            patch("/user/email", { newEmail }),
        changePassword: (oldPassword: string, newPassword: string) =>
            patch("/user/password", { oldPassword, newPassword })
    }
}