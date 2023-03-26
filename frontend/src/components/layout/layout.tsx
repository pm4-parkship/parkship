import React, { ReactNode } from 'react';
import { Button, Toolbar, Typography } from '@mui/material';
import { makeStyles, useTheme } from '@mui/styles';
import { toast } from 'react-toastify';
import { Icon } from '@iconify/react';
import { ErrorMapCtx, z, ZodIssueOptionalMessage } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { TextFieldElement } from 'react-hook-form-mui';
import { logger } from '../../logger';
import ImageCustom from '../image/image-custom';
import TableComponent from '../table/table';

export type LayoutProps = {
  children: ReactNode;
};

export function Layout({ children }: LayoutProps) {
  const classes = useStyles();
  const theme = useTheme();

  const formSchema = z.object({
    test0: z.string().min(2).max(20),
    test1: z
      .custom((data) => {
        if (typeof data === 'string') {
          return parseInt(data) <= 18;
        }
        return false;
      })
      .optional()
  });

  const customErrorMap = () => {
    return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
      if (issue.code === z.ZodIssueCode.custom) {
        if (issue.path.includes('test1')) {
          return {
            message: `It has to be between 1 and 18 or your age is ${issue.params}`
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

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  const handleSubmitCreate = (data) => {
    logger.log(data);
  };

  return (
    <>
      <Toolbar className={classes.topBarBottom}>
        <ImageCustom
          alt={'test'}
          width="2050"
          src={'/img/test.png'}
          localImage
        />
        <Icon icon="material-symbols:network-wifi-3-bar-rounded" />
        <Typography align="center" variant="body2" component="span">
          This is my Typography component
        </Typography>
        <Typography align="center" variant="body2" component="span">
          This is my Typography component
        </Typography>
        <Typography align="center" variant="body2" component="span">
          This is my Typography component
        </Typography>
        <Button
          onClick={() => toast.error('test')}
          variant="contained"
          color="primary"
        >
          This is my Button component
        </Button>
      </Toolbar>
      <main>
        <div className={classes.root}>
          <form
            style={{ display: 'grid', width: '100%', gap: '10px' }}
            onSubmit={handleSubmit((data) => handleSubmitCreate(data))}
          >
            <TextFieldElement
              placeholder="e.g Test"
              control={control}
              name="test0"
              label="Test 0"
              variant="outlined"
              fullWidth
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
            <TableComponent></TableComponent>
            <Button type={'submit'} variant={'contained'} color={'primary'}>
              Submit
            </Button>
          </form>
          {children}
        </div>
      </main>
      <div className={classes.toggleBtn}>Bottom Bar</div>
    </>
  );
}

const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: '1600px',
    margin: '0 auto',
    background: theme.palette.background.default
  },
  topBarBottom: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    overflow: 'hidden'
  },
  toggleBtn: {
    marginRight: 20,
    display: 'block',
    [theme.breakpoints.down('md')]: {
      marginRight: 5,
      display: 'none'
    }
  }
}));
