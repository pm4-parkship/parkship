import Head from 'next/head'
import Image from 'next/image'
import { Titillium_Web as Font } from 'next/font/google'
import styles from '@/styles/Home.module.css'
import { useEffect, useRef, useState } from 'react'

const font = Font({ subsets: ['latin'], weight: "600" })

export default function Home() {
  const [isLoading, setIsLoading] = useState(false)
  const [todos, setTodos] = useState([])
  const [error, setError] = useState("")

  useEffect(() => {
    if(isLoading) return
    setIsLoading(true)
    const fetchTodos = async () => {
      try {
        const response = await fetch("/api/todos")

        if(!response.ok) {
          throw new Error(response.statusText, { cause: response })
        }

        const todos = await response.json()
        setTodos(todos)
      } catch(e: any) {
        setError(e?.message || "An error occured")
      } finally {
        setIsLoading(false)
      }
    }
    fetchTodos()
  }, [])

  return (
    <>
      <main className={font.className}>
        { isLoading && <p>Loading todos...</p>}
        { error && <p style={{ color: "red" }}>{error}</p>}
        <pre>{JSON.stringify(todos, null, 4)}</pre>
      </main>
    </>
  )
}
