// // src/pages/UserDataTable.js
import React, { useState, useEffect } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { frFR } from '@mui/x-data-grid/locales';
import { frFR as coreFrFR } from '@mui/material/locale';
import { Paper, Typography, Box, TextField, InputAdornment, Button, Snackbar, Alert, ThemeProvider, createTheme } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { ListAlt } from '@mui/icons-material';
import UserActionsModal from './UserActionsModal';
import EditUserModal from './EditUserModal';
import UserService from '../../services/userService';
import ConfirmationDialog from './ConfirmationDialog';



const columns = [
  { field: 'id', headerName: 'ID', width: 70 },
  { field: 'nom', headerName: 'Nom', width: 130 },
  { field: 'prenom', headerName: 'Prénom', width: 130 },
  { field: 'email', headerName: 'Email', width: 200 },
  { field: 'telephone', headerName: 'Téléphone', width: 130 },
  { field: 'role', headerName: 'Rôle', width: 130 },
];

const theme = createTheme({
  palette: {
    primary: { main: '#1976d2' },
    secondary: { main: '#dc004e' },
  },
}, frFR, coreFrFR);

const UserDataTable = ({ userType, userData }) => {
  const [users, setUsers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [openActionsModal, setOpenActionsModal] = useState(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [openErrorSnackbar, setOpenErrorSnackbar] = useState(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [editUserData, setEditUserData] = useState({});
  const [usersToEdit, setUsersToEdit] = useState([]);
  const [currentEditIndex, setCurrentEditIndex] = useState(0);
  const [openConfirmationDialog, setOpenConfirmationDialog] = useState(false);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const fetchedUsers = await UserService.listUsers(userType);

        setUsers(fetchedUsers);
      } catch (error) {
        console.error("Erreur lors de la récupération des utilisateurs :", error);
      }
    };

    fetchUsers();
  }, [userType, userData]);


  const filteredUsers = users.filter(user =>
    Object.values(user).some(value =>
      value.toString().toLowerCase().includes(searchTerm.toLowerCase())
    )
  );

  const handleSelectionChange = (newSelection) => {
    setSelectedUsers(newSelection);
  };

  const handleOpenActionsModal = () => {
    setOpenActionsModal(true);
  };

  const handleCloseActionsModal = () => {
    setOpenActionsModal(false);
  };

  const handleOpenConfirmationDialog = () => {
    setOpenConfirmationDialog(true);
  };

  const handleCloseConfirmationDialog = () => {
    setOpenConfirmationDialog(false);
  };


  const handleDelete = async () => {
    try {
      for (let userId of selectedUsers) {
        await UserService.deleteUsers(userId);
      }

      const usersAfterDeletion = users.filter(user => !selectedUsers.includes(user.id));
      setUsers(usersAfterDeletion);

      // Fermer les modals
      setOpenActionsModal(false);
      setOpenConfirmationDialog(false);

      // Afficher le Snackbar après succès
      setOpenSnackbar(true);

    } catch (error) {
      console.error("Erreur lors de la suppression des utilisateurs:", error);

      // Fermer les modals en cas d'erreur
      setOpenActionsModal(false);
      setOpenConfirmationDialog(false);

      // Afficher le Snackbar d'erreur
      setOpenErrorSnackbar(true);
    }
  };


  const handleModify = () => {
    if (selectedUsers.length > 0) {
      const usersToModify = users.filter(user => selectedUsers.includes(user.id));
      setUsersToEdit(usersToModify);
      setCurrentEditIndex(0);
      setEditUserData(usersToModify[0]); // Pour commencer avec le premier utilisateur
      setOpenEditModal(true);
    }
    handleCloseActionsModal();
  };

  const handleUpdateUsers = (updatedUser) => {
    setUsers(prevUsers =>
      prevUsers.map(user =>
        user.id === updatedUser.id ? updatedUser : user
      )
    );
  };

  return (
    <ThemeProvider theme={theme}>
      <Paper elevation={3} sx={{ p: 3, m: 2 }}>
        <Typography variant="h4" component="div" sx={{ mb: 3, fontSize: 24, color: 'primary.main', fontWeight: 'bold', display: 'flex', alignItems: 'center' }}>
          <ListAlt sx={{ fontSize: 40, color: 'primary.main', mr: 0.5 }} />
          Liste des {userType}s
        </Typography>
        <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <TextField
            sx={{ width: '30%' }}
            variant="outlined"
            placeholder="Rechercher..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />
          <Button
            variant="contained"
            color="primary"
            onClick={handleOpenActionsModal}
            disabled={selectedUsers.length === 0}
          >
            Gérer les utilisateurs
          </Button>
        </Box>
        <div style={{ height: 500, width: '100%' }}>
          <DataGrid
            rows={filteredUsers}
            columns={columns}
            pageSize={5}
            rowsPerPageOptions={[5, 10, 20]}
            checkboxSelection
            onRowSelectionModelChange={(newSelection) => handleSelectionChange(newSelection)}
            sx={{
              pb: 4,
              '& .MuiDataGrid-columnHeaders': {
                backgroundColor: 'primary.main',
                color: 'white',
                fontSize: 16,
              },
              '& .MuiDataGrid-cell:hover': {
                color: 'primary.main',
              },
            }}
          />
        </div>
      </Paper>

      <UserActionsModal
        open={openActionsModal}
        onClose={handleCloseActionsModal}
        onModify={handleModify}
        onDelete={handleOpenConfirmationDialog}
        selectedUsers={selectedUsers}
      />

      <EditUserModal
        open={openEditModal}
        onClose={() => setOpenEditModal(false)}
        editUserData={editUserData}
        setEditUserData={setEditUserData}
        usersToEdit={usersToEdit}
        currentEditIndex={currentEditIndex}
        setCurrentEditIndex={setCurrentEditIndex}
        setUsersToEdit={setUsersToEdit}
        setOpenSnackbar={setOpenSnackbar}
        setOpenErrorSnackbar={setOpenErrorSnackbar}
        onUpdateUsers={handleUpdateUsers}
        userRole={userData && userData.role}
      />

      <ConfirmationDialog
        open={openConfirmationDialog}
        onClose={handleCloseConfirmationDialog}
        onConfirm={handleDelete}  // Supprimer les utilisateurs après confirmation
        message={`Êtes-vous sûr de vouloir supprimer ${selectedUsers.length} utilisateur(s) ?`}
      />

      <Snackbar
        open={openSnackbar}
        autoHideDuration={2000} // 2 secondes
        onClose={() => setOpenSnackbar(false)}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert onClose={() => setOpenSnackbar(false)} severity="success" sx={{ width: '100%' }}>
          Opération réalisée avec succès !
        </Alert>
      </Snackbar>

      <Snackbar
        open={openErrorSnackbar}
        autoHideDuration={2000} // 2 secondes
        onClose={() => setOpenErrorSnackbar(false)}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert onClose={() => setOpenErrorSnackbar(false)} severity="error" sx={{ width: '100%' }}>
          Erreur lors de la suppression des utilisateurs. Veuillez réessayer !
        </Alert>
      </Snackbar>


    </ThemeProvider>
  );
}

export default UserDataTable;
