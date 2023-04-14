import * as React from 'react';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import {logger} from '../../logger';
import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import {ErrorMapCtx, z, ZodIssueOptionalMessage} from 'zod';
import {TextFieldElement} from "react-hook-form-mui";


export default function LoginWindow() {
    const formSchema = z.object({
        email: z.string().email(),
        //password: z.string().min(1), //TODO Passwortprüfung kommt backend?
    });

    const customErrorMap = () => {
        return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
            // if (issue.code === z.ZodIssueCode.invalid_string) {
            //     if (issue.path.includes('password')) {
            //         return { message: 'Bitte geben Sie ein Passwort ein!' };
            //     }
            // }
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


    return (
        <div>
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
                    style={{ display: 'grid', width: '25%', gap: '20px' }}
                    onSubmit={handleSubmit((data) => logger.log(data))} //TODO Backend methode
                >
                    <Box>
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
                            sx={{ width: "100%" }}
                        >
                            Einloggen
                        </Button>
                    </Box>
                </form>
            </Box>
        </div>
    );
}
