import { useRef, useState, useEffect } from "react"
import { User } from '../../pages/api/user';

export async function fetchSession() {
    const response = await fetch("/api/user")

    if (!response.ok) {
        return Promise.reject()
    }

    const data = await response.json()
    return data
}

export default function useSession() {
    const initRef = useRef<boolean>()
    const [isLoading, setIsLoading] = useState<boolean>(true)
    const [session, setSession] = useState<User | undefined>()

    useEffect(() => {
        if (initRef.current) return
        initRef.current = true
        setIsLoading(true)
        const loadSession = async () => {
            try {
                const data = await fetchSession()
                setSession(data)
            } catch (e) {
                console.error("Not logged in!")
            } finally {
                setIsLoading(false)
            }
        }
        loadSession()
    }, [])

    const signIn = (data) => setSession(data)
    const signOut = async () => {
        await fetch("/api/logout")
        setSession(undefined)
    }

    return {
        isInitialized: isLoading === false,
        isSignedIn: session !== undefined,
        user: session,
        signIn,
        signOut
    }

}