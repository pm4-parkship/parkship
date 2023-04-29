import { useEffect } from 'react';
import { useRouter } from 'next/router';
import useSWR from 'swr';
import { User } from '../../pages/api/user';

export default function useUser({
  redirectTo = '/login',
  redirectIfFound = false
} = {}) {
  const { data: user, mutate: mutateUser } = useSWR<User>('login', {
    revalidateOnReconnect: false,
    revalidateOnFocus: false,
    keepPreviousData: true
  });
  const loggedOutUser: User = {
    isLoggedIn: false,
    username: '',
    token: '',
    role: ''
  };
  const router = useRouter();
  useEffect(() => {
    // if no redirect needed, just return (example: already on /dashboard)
    // if user data not yet there (fetch in progress, logged in or not) then don't do anything yet
    if (!redirectTo || !user) return;

    if (
      // If redirectTo is set, redirect if the user was not found.
      (redirectTo && !redirectIfFound && !user?.isLoggedIn) ||
      // If redirectIfFound is also set, redirect if the user was found
      (redirectIfFound && user?.isLoggedIn)
    ) {
      router.push(redirectTo);
    }
  }, [user, redirectIfFound, redirectTo]);

  return { user: user || loggedOutUser, mutateUser };
}
