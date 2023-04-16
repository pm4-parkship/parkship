import React, { useState } from 'react';
import fetchJson, { FetchError } from '../src/fetch-json/fetch-json';
import { logger } from '../src/logger';
import Form from '../src/auth/form';
import useUser from '../src/auth/use-user';
import {ErrorMapCtx, z, ZodIssueOptionalMessage} from 'zod';
import {zodResolver} from '@hookform/resolvers/zod';
import {useForm} from 'react-hook-form';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import {TextFieldElement} from "react-hook-form-mui";

export default function Login() {
    // here we just check if user is already logged in and redirect to profile
    const { mutateUser } = useUser({
        redirectTo: '/profile/',
        redirectIfFound: true
    });


    const formSchema = z.object({
        email: z.string().email(),
        //password: z.string().min(1), //TODO Passwortprüfung kommt vom backend?
    });

    const customErrorMap = () => {
        return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
            if(issue.code === z.ZodIssueCode.invalid_string){
                if (issue.path.includes('email')) {
                    return {
                        message: `Bitte geben Sie eine korrekte Email ein!`
                    };
                }
            }
            return {message: ctx.defaultError};
        };
    };

    const {handleSubmit, control} = useForm({
        resolver: zodResolver(formSchema, {
            errorMap: customErrorMap()
        }),
        defaultValues: {
            email: '',
            password: ''
        }
    });


    const [errorMsg, setErrorMsg] = useState('');

    return (
        <div>
            <div className="login">
                <Form
                    errorMessage={errorMsg}
                    onSubmit={async function handleSubmit(event) {
                        event.preventDefault();

                        const body = {
                            email: event.currentTarget.email.value,
                            password: event.currentTarget.password.value,
                        };

                        try {
                            await mutateUser(
                                await fetchJson('/backend/auth/signin', {
                                    method: 'POST',
                                    credentials: 'include',
                                    headers: { 'Content-Type': 'application/json' },
                                    body: JSON.stringify(body),
                                })
                            );
                        } catch (error) {
                            if (error instanceof FetchError) {
                                setErrorMsg(error.data.message);
                            } else {
                                logger.error('An unexpected error happened:', error);
                            }
                        }
                    }}
                />
            </div>
            <Box
                sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'
                }}
            >
                <Typography component="h1" variant="h5">
                    Willkommen bei Parkship!
                </Typography>
                <form
                    style={{ display: 'grid', width: '25%', gap: '20px'}}
                    onSubmit={handleSubmit((data) => logger.log(data))} //TODO Backend methode
                >
                    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', p: 4, bgcolor: 'background.paper' }}>
                        <TextFieldElement
                            required
                            fullWidth
                            id="email"
                            label="Email Addresse"
                            name="email"
                            autoComplete="email"
                            autoFocus
                            control={control}
                            style={{ marginTop: '20px' }}

                        />
                        <TextFieldElement
                            required
                            fullWidth
                            name="password"
                            label="Passwort"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            control={control}
                            autoFocus
                            style={{ marginTop: '20px' }}
                        />
                        <Button
                            type={"submit"}
                            variant={"contained"}
                            sx={{ width: "100%", marginTop: '20px'}}
                        >
                            Einloggen
                        </Button>
                    </Box>
                </form>
            </Box>
        </div>
    );
}

