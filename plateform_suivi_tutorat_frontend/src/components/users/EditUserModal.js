import React from 'react';
import { Modal, Fade, Box, Typography, TextField, Button, FormControl, InputLabel, Select, MenuItem, } from '@mui/material';
import AuthService from '../../services/authService';

const EditUserModal = ({
  open,
  onClose,
  editUserData,
  setEditUserData,
  usersToEdit,
  currentEditIndex,
  setCurrentEditIndex,
  setUsersToEdit,
  setOpenSnackbar,
  setOpenErrorSnackbar,
  onUpdateUsers,
  userRole
}) => {
  const handleEditChange = (event) => {
    setEditUserData({ ...editUserData, [event.target.name]: event.target.value });
  };

  const handleSave = async () => {
    // Appel à votre service pour mettre à jour l'utilisateur
    try {
      await AuthService.UpdateUser(editUserData, editUserData.id);

      // Mettre à jour l'utilisateur modifié
      setUsersToEdit((prevUsers) => {
        const newUsers = [...prevUsers];
        newUsers[currentEditIndex] = editUserData;
        return newUsers;
      });

      // Appeler la fonction pour mettre à jour l'état dans UserDataTable
      onUpdateUsers(editUserData); // Assurez-vous de passer la fonction appropriée

      setOpenSnackbar(true);
      // Ne pas fermer le modal ici
    } catch (error) {
      console.error("Erreur lors de la mise à jour de l'utilisateur :", error);
      setOpenErrorSnackbar(true);
    }
  };

  const handleNext = () => {
    // Sauvegarder l'utilisateur actuel avant de passer au suivant
    handleSave();

    if (currentEditIndex < usersToEdit.length - 1) {
      setCurrentEditIndex(currentEditIndex + 1);
      setEditUserData(usersToEdit[currentEditIndex + 1]);
    }
  };

  const handlePrev = () => {
    // Sauvegarder l'utilisateur actuel avant de passer au précédent
    handleSave();

    if (currentEditIndex > 0) {
      setCurrentEditIndex(currentEditIndex - 1);
      setEditUserData(usersToEdit[currentEditIndex - 1]);
    }
  };

  return (
    <Modal open={open} onClose={onClose} closeAfterTransition>
      <Fade in={open}>
        <Box sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          width: 400,
          bgcolor: 'background.paper',
          boxShadow: 24,
          p: 4,
          borderRadius: 2,
        }}>
          <Typography variant="h6" component="h2" gutterBottom>
            Modifier l'utilisateur
          </Typography>
          <TextField
            margin="normal"
            fullWidth
            label="Nom"
            name="nom"
            value={editUserData.nom}
            onChange={handleEditChange}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Prénom"
            name="prenom"
            value={editUserData.prenom}
            onChange={handleEditChange}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Email"
            name="email"
            value={editUserData.email}
            onChange={handleEditChange}
          />
          <TextField
            margin="normal"
            fullWidth
            label="Téléphone"
            name="telephone"
            value={editUserData.telephone}
            onChange={handleEditChange}
          />
          <FormControl fullWidth sx={{ mt: 2 }}>
            <InputLabel>Rôle</InputLabel>
            <Select
              name="role"
              value={editUserData.role}
              onChange={handleEditChange}
            >
              <MenuItem value="Tuteur">Tuteur</MenuItem>

              {userRole && userRole === "admin" && <MenuItem value="Administrateur">Administrateur</MenuItem>}
              {userRole && userRole === "admin" && <MenuItem value="Tracker">Tracker</MenuItem>}

            </Select>
          </FormControl>
          <Box sx={{ display: 'flex', gap: 1, justifyContent: 'space-between', my: 3 }}>
            <Button onClick={handlePrev} variant="outlined" disabled={currentEditIndex === 0}>
              Précédent
            </Button>
            <Button onClick={handleNext} variant="outlined" disabled={currentEditIndex === usersToEdit.length - 1}>
              Suivant
            </Button>

          </Box>

          <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <Button onClick={handleSave} variant="contained" color="primary" sx={{ width: 200 }}>
              Sauvegarder
            </Button>
            <Button onClick={onClose} variant="outlined" color="secondary" sx={{ width: 100 }}>
              Fermer
            </Button>
          </Box>
        </Box>
      </Fade>
    </Modal>
  );
};

export default EditUserModal;
