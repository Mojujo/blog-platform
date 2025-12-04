import { useEffect, useState } from "react";
import apiClient from "../api/apiClient";
import { useAuth } from "../context/AuthContext";

export interface UserProfile {
    id: string;
    username: string;
    email: string;
    roles: string[];
}

export const useProfile = () => {
    const { user } = useAuth();
    const [profile, setProfile] = useState<UserProfile | null>(null);
    const [loading, setLoading] = useState(false)
    
    const fetchProfile = async () => {

        setLoading(true);

        try {
            const response = await apiClient.get("/user/profile");
            setProfile(response.data);
        } catch (err: any) {
            console.error("Failed to fetch profile", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (user) {
            fetchProfile();
        } else {
            setProfile(null);
        }
    }, [user]);

    return { profile, loading, fetchProfile};
};