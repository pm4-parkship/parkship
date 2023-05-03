import { zodResolver } from '@hookform/resolvers/zod';
import { Box, Button, Paper, Typography } from '@mui/material';
import { useRouter } from 'next/router';
import { useForm } from 'react-hook-form';
import { TextFieldElement } from 'react-hook-form-mui';
import { toast } from 'react-toastify';
import { ErrorMapCtx, ZodIssueOptionalMessage, z } from 'zod';
import { UserRole } from '../src/models';

export default function Login({ signIn }) {
  const router = useRouter();

  const formSchema = z.object({
    email: z.string().email(),
    password: z.string().min(1, 'Bitte geben Sie Ihr Passwort ein!')
  });

  const customErrorMap = () => {
    return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
      if (issue.code === z.ZodIssueCode.invalid_string) {
        if (issue.path.includes('username')) {
          return {
            message: `Bitte geben Sie eine korrekte Email ein!`
          };
        }
      }
      return { message: ctx.defaultError };
    };
  };

  const { handleSubmit, control } = useForm({
    resolver: zodResolver(formSchema, {
      errorMap: customErrorMap()
    }),
    mode: 'onSubmit',
    defaultValues: {
      email: '',
      password: ''
    }
  });

  const handleFormSubmit = async (data: {
    email: string;
    password: string;
  }) => {
    const body = {
      email: data.email,
      password: data.password
    };

    try {
      await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      }).then(async (res: any) => {
        if (res.ok) {
          const data = await res.json();
          const userData = {
            isLoggedIn: true,
            role: UserRole[data.user.role],
            token: data.user.token,
            username: data.user.username
          }
          signIn(userData);
          router.push(userData.role === UserRole.ADMIN ? "/admin/parking-lots" : "/search");
        }
      });
    } catch (error: any) {
      toast.error(error.message);
    }
  };

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
          style={{ display: 'grid', width: '25%', marginTop: '20px' }}
          onSubmit={handleSubmit((data) => handleFormSubmit(data))}
        >
          <Paper elevation={3}>
            <TextFieldElement
              required
              fullWidth
              id="email"
              placeholder="admin@parkship.ch"
              label="Email Addresse"
              name="email"
              autoComplete="email"
              autoFocus
              control={control}
              style={{ marginTop: '10px', height: '60px' }}
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
              style={{ marginTop: '30px', height: '60px' }}
            />
            <Button
              type={'submit'}
              variant={'contained'}
              sx={{
                width: '94%',
                marginTop: '30px'
              }}
            >
              Einloggen
            </Button>
          </Paper>
        </form>
      </Box>
    </div>
  );
}