import { useEffect } from "react"
import apiClient from "../api/apiClient"

export default function Home() {

    useEffect(() => {
        // GET request to trigger backend's CsrfCookieFilter
        apiClient.get("/");
    }, []);

    return (
        <>
        
        </>
    )
}