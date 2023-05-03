import { useEffect } from "react"
import { useRouter } from "next/router"

export function useAuthRedirect(pageProps: any, userData: any) {
    const router = useRouter()
    const { isInitialized, isSignedIn } = userData;

    useEffect(() => {
        if (!router.isReady || !isInitialized) return

        if (!isSignedIn && !router.asPath.includes("login")) {
            router.replace(`/login?url=${router.asPath}`, undefined, { shallow: true })
        }

        if (isSignedIn && router.asPath.includes("login")) {
            router.replace("/", "/", { shallow: true })
        }
    }, [pageProps, router.isReady, isInitialized, isSignedIn])
}
