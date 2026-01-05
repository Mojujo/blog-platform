import apiClient from "../api/apiClient"

export function useRegister() {
    const register = async (username: string, email: string, password: string) => {
        try {
            await apiClient.post("/user/register", { username, email, password });
            return { success: true, error: null };
        } catch (err: any) {
            const data = err.response?.data;
            let errorMessage = "Registration Failed";

            if (data?.detail) {
                errorMessage = data.detail;
            }

            else if (data?.errors && Array.isArray(data.errors) && data.errors.length > 0) {
                errorMessage = data.errors[0].defaultMessage;
            }

            else if (data?.message) {
                errorMessage = data.message;
            }

            console.error("Registration Error Detail:", data);

            return { success: false, error: errorMessage };
        }
    };

    return { register };
}