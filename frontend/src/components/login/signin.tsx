import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { createTheme } from '@mui/material/styles';
import { logger } from '../../logger';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { ErrorMapCtx, z, ZodIssueOptionalMessage } from 'zod';
import { TextFieldElement } from 'react-hook-form-mui';

//TODO change Pages Name and Link
function Copyright(props: any) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {'Copyright © '}
      <Link color="inherit" href="https://papertoilet.com/">
        www.parkship.dumm
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

const theme = createTheme();

export default function SignIn() {
  const formSchema = z.object({
    test0: z.string().min(2).max(20),
    test1: z.custom((data) => {
      if (typeof data === 'string') {
        return parseInt(data) <= 18;
      }
      return true;
    })
  });

  const customErrorMap = () => {
    return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
      if (issue.code === z.ZodIssueCode.custom) {
        if (issue.path.includes('test1')) {
          return {
            message: `It has to be between 1 and 18`
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
    defaultValues: {
      test0: '',
      test1: ''
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
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Willkomen
        </Typography>
        <form
          style={{ display: 'grid', width: '100%', gap: '10px' }}
          onSubmit={handleSubmit((data) => logger.log(data))}
        >
          <TextFieldElement
            placeholder="e.g Test"
            control={control}
            name="test0"
            label="Test 0"
            variant="outlined"
            fullWidth
            required
          />
          <TextFieldElement
            placeholder="e.g Test"
            control={control}
            name="test1"
            label="Test 1"
            variant="outlined"
            fullWidth
            required
          />
          <Button type={'submit'} variant={'contained'} color={'primary'}>
            Submit
          </Button>
        </form>
        <Box component="form" noValidate sx={{ mt: 1 }}>
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="Name oder Email Addresse"
            name="email"
            autoComplete="email"
            autoFocus
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Passwort"
            type="password"
            id="password"
            autoComplete="current-password"
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Einloggen
          </Button>
          <Grid container>
            <Grid item xs>
              <Link href="#" variant="body2">
                Passwort vergessen?
              </Link>
            </Grid>
            <Grid item>
              <Link href="#" variant="body2">
                {'Keinen Account? Melde dich an!'}
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
      <Copyright sx={{ mt: 8, mb: 4 }} />
    </div>
  );
}
