import { zodResolver } from '@hookform/resolvers/zod';
import {
  Box,
  Button,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Modal,
  Select,
  SelectChangeEvent,
  Typography
} from '@mui/material';
import React, { useEffect, useState } from 'react';
import { TextFieldElement, useForm } from 'react-hook-form-mui';
import { toast } from 'react-toastify';
import { ErrorMapCtx, z, ZodIssueOptionalMessage } from 'zod';
import { makeStyles } from '@mui/styles';
import { Icon } from '@iconify/react';
import { User } from 'pages/api/user';
import ShowPasswordModal from './show-password-modal';
import {
  CreateUserModel,
  UserDto,
  UserRole,
  UserRoleLabel
} from 'src/models/user/user.model';
import apiClient from '../../../../pages/api/api-client';

const UserAdministrationModal = ({
  showModal = true,
  setShowModal,
  onAddedUser,
  user
}: {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  onAddedUser: (value: UserDto) => void;
  user: User;
}) => {
  const [roles, setRoles] = useState<UserRole[]>([]);
  const [role, setRole] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [openShowPasswordModal, setOpenShowPasswordModal] = useState(false);
  const classes = useStyles();

  const formSchema = z.object({
    firstname: z.string().min(3),
    lastname: z.string().min(1),
    email: z.string().email()
  });

  useEffect(() => {
    apiClient()
      .admin.getUserRoles(user)
      .then((result) => setRoles(result))
      .catch(() =>
        toast.error(
          `Die Rollen konnten nicht geladen werden. Versuchen Sie es später nochmal`
        )
      );
  }, []);

  const customErrorMap = () => {
    return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
      if (
        issue.code === z.ZodIssueCode.invalid_string ||
        issue.code === z.ZodIssueCode.too_small
      ) {
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
            message: `Bitte geben Sie eine gültige E-Mail ein.`
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
    const body: CreateUserModel = {
      name: data.firstname,
      surname: data.lastname,
      email: data.email,
      role: UserRole[role]
    };

    apiClient()
      .admin.createUser(user, body)
      .then((result) => {
        onAddedUser(result);
        setPassword(result.password);
        setOpenShowPasswordModal(true);
        toast.success(`Benutzer ${body.name} wurde erfolgreich gespeichert`);
      })
      .catch(() =>
        toast.error(
          `Benutzer konnte nicht gespeichert werden. Versuchen Sie es später nochmal`
        )
      );
  };

  const handleRoleChange = (event: SelectChangeEvent<typeof role>) => {
    setRole(event.target.value);
  };

  const closePasswordModal = () => {
    control._reset();
    setRole('');
    setShowModal(false);
  };

  return (
    <>
      <div>
        <Modal open={showModal} onClose={() => closePasswordModal()}>
          <Box sx={style}>
            <div className={classes.closeIconOnHeader}>
              <Icon
                onClick={() => closePasswordModal()}
                className={classes.closeIcon}
                icon="ci:close-big"
              />
            </div>
            <form onSubmit={handleSubmit((data) => handleFormSubmit(data))}>
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
                label="E-Mail-Adresse"
                name="email"
                autoComplete="email"
                autoFocus
                control={control}
                style={{ marginTop: '20px' }}
              />

              <FormControl fullWidth>
                <InputLabel required id="role-label" sx={{ marginTop: '20px' }}>
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
                  {roles.map((role) => (
                    <MenuItem value={role} key={role}>
                      {UserRoleLabel.get(role)}
                    </MenuItem>
                  ))}
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
            </form>
          </Box>
        </Modal>

        <ShowPasswordModal
          showModal={openShowPasswordModal}
          setShowModal={setOpenShowPasswordModal}
          password={password}
        />
      </div>
    </>
  );
};

const useStyles = makeStyles(() => ({
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
  borderRadius: 4,
  p: 4
};

export default UserAdministrationModal;
