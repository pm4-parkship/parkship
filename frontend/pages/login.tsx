import { zodResolver } from '@hookform/resolvers/zod';
import { Box, Button, Paper, Typography } from '@mui/material';
import { useRouter } from 'next/router';
import { useForm } from 'react-hook-form';
import { TextFieldElement } from 'react-hook-form-mui';
import { toast } from 'react-toastify';
import { UserRole } from '../src/models';
import { ErrorMapCtx, z, ZodIssueOptionalMessage } from 'zod';
import { makeStyles } from '@mui/styles';

export default function Login({ signIn }) {
  const router = useRouter();
  const classNames = useStyles();

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
            username: data.user.username,
            surname: data.user.username,
          };
          signIn(userData);
          router.push(
            userData.role === UserRole.ADMIN ? '/admin/parking-lots' : '/search'
          );
        }
      });
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  return (
    <div style={{ margin: 'auto 0' }}>
      <Box className={classNames.root}>
        <Typography component="h1" variant="h5">
          Willkommen bei Parkship!
        </Typography>
        <form
          className={classNames.form}
          onSubmit={handleSubmit((data) => handleFormSubmit(data))}
        >
          <Paper elevation={3} className={classNames.container}>
            <TextFieldElement
              required
              fullWidth
              id="email"
              placeholder="mail@domain.ch"
              label="Email Addresse"
              name="email"
              autoComplete="email"
              autoFocus
              control={control}
              className={classNames.textField}
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
              className={classNames.textField}
            />
            <Button
              type={'submit'}
              variant={'contained'}
              className={classNames.button}
            >
              Einloggen
            </Button>
          </Paper>
        </form>
      </Box>
    </div>
  );
}
const useStyles = makeStyles(() => ({
  container: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    width: '100%',
    padding: 16,
    paddingTop: 45,
    rowGap: 35
  },
  textField: { height: '60px' },
  root: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center'
  },
  form: { display: 'grid', marginTop: '20px' },
  button: {
    width: '80%'
  }
}));

export async function getStaticProps() {
  return {
    props: {
      publicPage: true
    }
  };
}
