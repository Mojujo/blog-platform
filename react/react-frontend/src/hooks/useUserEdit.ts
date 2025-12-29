import { useState } from "react"
import apiClient from "../api/apiClient";
import { useAuth } from "../context/AuthContext";

type Status = "idle" | "loading" | "success" | "error";

export const useUserEdit = () => {
    const [status, setStatus] = useState<Status>("idle");
    const [error, setError] = useState<string | null>(null);
    const { logout } = useAuth();

    const patch = async (url: string, body: object) => {
        
        setStatus("loading")
        setError(null);

        try {

            await apiClient.patch(url, body);
            setStatus("success");
            logout();
            return true;

        } catch (err: any) {

            setStatus("error");
            setError(err?.response?.data?.detail ?? "Error updating profile");
            return false;

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