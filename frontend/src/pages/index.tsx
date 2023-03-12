import Head from 'next/head'
import Image from 'next/image'
import { Inter } from 'next/font/google'
import styles from '@/styles/Home.module.css'
import { useEffect, useState } from 'react'

const inter = Inter({ subsets: ['latin'] })

export default function Home() {
  const [joke, setJoke] = useState("")

  useEffect(() => {
    const fetchJoke = async () => {
      const response = await fetch("http://localhost:8080/api/dadjokes")
      const text = await response.text()
      setJoke(text)
    }
    fetchJoke()
  }, [])

  return (
    <>
      <main>
        <h1>Here is a dad joke:</h1>
        <p>{joke}</p>
      </main>
    </>
  )
}
