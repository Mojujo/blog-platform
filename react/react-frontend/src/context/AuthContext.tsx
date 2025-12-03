import { createContext, useState, ReactNode, useContext, useEffect } from "react";
import { getXsrfToken } from "../util/csrfUtil";
import apiClient from "../api/apiClient";
import type { AxiosRequestConfig } from "axios";

type User = {
    username: string;
    roles: string[];
};

type AuthContextType = {
    user: User | null;
    isLoggedIn: boolean;
    login: (username: string, password: string) => Promise<boolean>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {

    const [user, setUser] = useState<User | null>(null);

    // Axios interceptor to automatically attach CSRF tokens
    useEffect(() => {
        const interceptor = apiClient.interceptors.request.use(config => {

            if (!config.headers) {
                config.headers = {} as any;
            }

            const token = getXsrfToken();
            if (token) {
                (config.headers as any)["X-XSRF-TOKEN"] = token;
            }

            config.withCredentials = true; // always send cookies
            return config;
        });

        return () => {
            apiClient.interceptors.request.eject(interceptor);
        };
    }, []);

    const refreshUser = async () => {

        try {
            await apiClient.get("/user/csrf");

            const response = await apiClient.get("/auth/me");
            setUser({
                username: response.data.username,
                roles: response.data.roles
            });

        } catch (err: any) {
            if (err.response?.status === 401) {
                setUser(null);
            } else {
                console.error("Failed to refresh user", err)
            }
        }
    };

    useEffect(() => {
        refreshUser();
    }, []);

    const login = async (username: string, password: string): Promise<boolean> => {

        try {
            const response = await apiClient.post("/auth/login", { username, password });

            setUser({
                username: response.data.username,
                roles: response.data.roles
            });

            return true;

        } catch (err: any) {
            setUser(null);
            return false;
        }
    };

    const logout = async (): Promise<void> => {

        try {
            await apiClient.post("auth/logout");

        } catch (err: any) {
            console.error("Logout Failed", err)
        }

        setUser(null);
    };

    return (
        <AuthContext.Provider value={{
            user,
            isLoggedIn: user !== null,
            login,
            logout
        }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("Must be used within AuthProvider");
    return ctx;
};