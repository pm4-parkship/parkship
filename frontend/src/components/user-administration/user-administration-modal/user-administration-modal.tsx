import { zodResolver } from '@hookform/resolvers/zod';
import {
  Box,
  Button,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Modal,
  Paper,
  Select,
  SelectChangeEvent,
  Typography
} from '@mui/material';
import React from 'react';
import { TextFieldElement, useForm } from 'react-hook-form-mui';
import { toast } from 'react-toastify';
import { z, ZodIssueOptionalMessage, ErrorMapCtx } from 'zod';
import { makeStyles } from '@mui/styles';
import { Icon } from '@iconify/react';

const UserAdministrationModal = ({
  showModal = true,
  setShowModal
}: {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
}) => {
  const classes = useStyles();

  const formSchema = z.object({
    firstname: z.string().min(3),
    lastname: z.string().min(1),
    email: z.string().email()
  });

  const rolesMocked = ['Admin', 'Benutzer'];

  const getRoles = (): string[] | null => {
    // Frage an Rabus: Wohin damit?
    try {
      fetch('/backend/users/roles', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
      }).then(async (response) => {
        // const rolesData = await response.json();
        // logger.log(rolesData);

        const tempRoles: any[] = [];

        rolesMocked.forEach((role) => {
          // replace rolesMocked with rolesData from response (Safiyya)
          tempRoles.push(
            <MenuItem value={role} key={role}>
              {role}
            </MenuItem>
          );
        });
        setRoles(tempRoles);
      });
    } catch (error: any) {
      toast.error(error.message);
      return null;
    }
    return null;
  };

  //Todo: Rolle von Backend erhalten mittels GET und dementsprechend anzeigen
  const [roles, setRoles] = React.useState<any[] | null>(() => getRoles());
  const [role, setRole] = React.useState<string>('');

  const customErrorMap = () => {
    return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
      if (issue.code === z.ZodIssueCode.invalid_string) {
        if (issue.path.includes('firstname')) {
          return {
            message: `Bitte geben Sie einen gültigen Vornamen ein.`
          };
        }
        if (issue.path.includes('lastname')) {
          return {
            message: `Bitte geben Sie einen gültigen Nachnamen ein.`
          };
        }
        if (issue.path.includes('email')) {
          return {
            message: `Bitte geben Sie eine korrekte Email ein.`
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
      firstname: '',
      lastname: '',
      email: '',
      roles: ''
    }
  });

  const handleFormSubmit = async (data: {
    firstname: string;
    lastname: string;
    email: string;
  }) => {
    const body = {
      firstname: data.firstname,
      lastname: data.lastname,
      email: data.email,
      role: role
    };

    try {
      // TODO Backend API endpoint hinzufügen und hier im frontend anpassen.
      await fetch('/backend/user/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      }).then(async (res: any) => {
        if (res.ok) {
          const data = await res.json();
        }
      });
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  const handleRoleChange = (event: SelectChangeEvent<typeof role>) => {
    setRole(event.target.value);
  };

  return (
    <>
      <div>
        <Modal open={showModal} onClose={() => setShowModal(false)}>
          <Box sx={style}>
            <div className={classes.closeIconOnHeader}>
              <Icon
                onClick={() => setShowModal(false)}
                className={classes.closeIcon}
                icon="ci:close-big"
              />
            </div>
            <form onSubmit={handleSubmit((data) => handleFormSubmit(data))}>
              <Paper>
                <Typography align="center" component="h1" variant="h5">
                  Benutzer hinzufügen
                </Typography>

                <TextFieldElement
                  required
                  fullWidth
                  name="firstname"
                  label="Vorname"
                  id="firstname"
                  control={control}
                  autoFocus
                  style={{ marginTop: '20px' }}
                />

                <TextFieldElement
                  required
                  fullWidth
                  name="lastname"
                  label="Nachname"
                  id="lastname"
                  control={control}
                  autoFocus
                  style={{ marginTop: '20px' }}
                />

                <TextFieldElement
                  required
                  fullWidth
                  id="email"
                  placeholder="E-Mail"
                  label="Email Addresse"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  control={control}
                  style={{ marginTop: '20px' }}
                />

                <FormControl fullWidth>
                  <InputLabel
                    required
                    id="role-label"
                    sx={{ marginTop: '20px' }}
                  >
                    Rolle
                  </InputLabel>
                  <Select
                    required
                    labelId="role-label"
                    id="role"
                    label="Rolle"
                    value={role}
                    onChange={handleRoleChange}
                    sx={{ width: '100%', marginTop: '20px' }}
                  >
                    {roles}
                  </Select>
                </FormControl>

                <Grid container justifyContent="flex-end">
                  <Button
                    type={'submit'}
                    variant={'contained'}
                    sx={{
                      width: '50%',
                      marginTop: '20px'
                    }}
                  >
                    Hinzufügen
                  </Button>
                </Grid>
              </Paper>
            </form>
          </Box>
        </Modal>
      </div>
    </>
  );
};

const useStyles = makeStyles((theme) => ({
  closeIconOnHeader: {
    right: '2%',
    float: 'right'
  },
  closeIcon: {
    height: '20px',
    width: '20px',
    '&:hover': {
      opacity: '50%'
    }
  }
}));

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'white',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4
};

export default UserAdministrationModal;
