import React, { useState, useEffect } from 'react';
import {
    Grid,
    Card,
    CardContent,
    Typography,
    IconButton,
    Box,
    TextField,
    InputAdornment,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Snackbar,
    Alert,
    Divider,
} from '@mui/material';
import { Edit, Delete, Search, Add, Groups } from '@mui/icons-material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import GroupForm from './GroupForm';
import GroupeService from '../../services/groupeService';

const theme = createTheme({
    palette: {
        primary: { main: '#1976d2' },
        secondary: { main: '#dc004e' },
    },
});

const GroupCard = ({ group, onEdit, onDelete }) => (
    <Card elevation={2} sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
        <CardContent sx={{ flexGrow: 1 }}>
            <Typography variant="h6" component="div" gutterBottom>
                {group.nom}
            </Typography>
            <Typography variant="body2" color="text.secondary">
                Module: {group.module.nom}
            </Typography>
        </CardContent>
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', p: 1 }}>
            <IconButton onClick={() => onEdit(group && group)} aria-label="modifier" sx={{ color: '#3B5998' }}>
                <Edit />
            </IconButton>
            <IconButton onClick={() => onDelete(group)} aria-label="supprimer" sx={{ color: 'rgb(143, 16, 16)' }} >
                <Delete />
            </IconButton>
        </Box>
    </Card>
);

const GroupCardList = () => {
    const [groups, setGroups] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [editGroup, setEditGroup] = useState(null);
    const [deleteGroup, setDeleteGroup] = useState(null);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [openAddModal, setOpenAddModal] = useState(false);
    const [apiError, setApiError] = useState('');

    useEffect(() => {
        fetchGroups();
    }, []);

    const fetchGroups = async () => {
        try {
            const groupsData = await GroupeService.getAllGroupes();
            setGroups(groupsData);
        } catch (error) {
            console.error("Erreur lors de la récupération des groupes:", error);
            setApiError("Erreur lors de la récupération des groupes. Veuillez réessayer.");
        }
    };

    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    const filteredGroups = groups.filter(group =>
        group.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
        group.module.nom.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleEdit = (group) => {
        setEditGroup(group);
    };

    const handleDelete = (group) => {
        setDeleteGroup(group);
    };

    const handleCloseEdit = () => {
        setEditGroup(null);
    };

    const handleCloseDelete = () => {
        setDeleteGroup(null);
    };

    const handleSaveEdit = async (editedGroup) => {
        try {

            await GroupeService.UpdateGroupes(editedGroup, editedGroup.id);
            fetchGroups();
            setEditGroup(null);
            setSnackbarMessage('Groupe modifié avec succès');
            setOpenSnackbar(true);
        } catch (error) {
            console.error("Erreur lors de la modification:", error);
            setApiError(error.response?.data?.message || "Erreur lors de la modification. Veuillez réessayer.");
        }
    };

    const handleConfirmDelete = async () => {
        try {
            await GroupeService.deleteGroupe(deleteGroup.id);
            fetchGroups();
            setDeleteGroup(null);
            setSnackbarMessage('Groupe supprimé avec succès');
            setOpenSnackbar(true);
        } catch (error) {
            console.error("Erreur lors de la suppression:", error);
            setApiError(error.response?.data?.message || "Erreur lors de la suppression. Veuillez réessayer.");
        }
    };

    const handleAddGroup = async (newGroup) => {
        try {
     
            await GroupeService.createGroupe(newGroup);
            fetchGroups();
            setOpenAddModal(false);
            setSnackbarMessage('Groupe ajouté avec succès');
            setOpenSnackbar(true);
        } catch (error) {
            console.error("Erreur lors de l'ajout du groupe:", error);
            setApiError(error.response?.data?.message || "Erreur lors de l'ajout du groupe. Veuillez réessayer.");
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Box sx={{ p: 3 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 4 }}>
                        <Groups sx={{ color: 'primary.main', fontSize: 40 }} />
                        <Typography variant="h4" sx={{ fontSize: 30, fontWeight: 'bold', color: 'primary.main' }}>
                            Gestion des Groupes
                        </Typography>
                    </Box>
                </Box>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        placeholder="Rechercher un groupe..."
                        value={searchTerm}
                        onChange={handleSearch}
                        sx={{ width: 300 }}
                        InputProps={{
                            startAdornment: (
                                <InputAdornment position="start">
                                    <Search />
                                </InputAdornment>
                            ),
                        }}
                    />
                    <Button
                        variant="contained"
                        startIcon={<Add />}
                        onClick={() => setOpenAddModal(true)}
                    >
                        Ajouter un groupe
                    </Button>
                </Box>
                <Divider sx={{ mb: 3 }} />
                <Grid container spacing={2}>
                    {filteredGroups.map(group => (
                        <Grid item xs={12} sm={6} md={4} key={group.id}>
                            <GroupCard
                                group={group}
                                onEdit={handleEdit}
                                onDelete={handleDelete}
                            />
                        </Grid>
                    ))}
                </Grid>
            </Box>

            {/* Dialog pour l'édition */}
            <Dialog open={!!editGroup} onClose={handleCloseEdit}>
                <DialogTitle>Modifier le groupe</DialogTitle>
                <DialogContent>
                    <GroupForm
                        initialData={editGroup}
                        onSubmit={handleSaveEdit}
                        onClose={handleCloseEdit}
                        apiError={apiError}
                        setApiError={setApiError}
                    />
                </DialogContent>
            </Dialog>

            {/* Dialog pour la suppression */}
            <Dialog
                open={!!deleteGroup}
                onClose={handleCloseDelete}
            >
                <DialogTitle>Confirmer la suppression</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Êtes-vous sûr de vouloir supprimer le groupe "{deleteGroup?.nom}" ?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDelete}>Annuler</Button>
                    <Button onClick={handleConfirmDelete} color="secondary">
                        Supprimer
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Dialog pour l'ajout */}
            <Dialog open={openAddModal} onClose={() => setOpenAddModal(false)}>
                <DialogTitle sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    mb: 3,
                }}>
                    <Groups sx={{ fontSize: 40, mr: 1, color: 'primary.main' }} />
                    <Typography component="h2" variant="h5" fontWeight='bold'>
                        Ajouter un nouveau groupe
                    </Typography>
                </DialogTitle>
                <DialogContent>
                    <GroupForm
                        onSubmit={handleAddGroup}
                        onClose={() => setOpenAddModal(false)}
                        apiError={apiError}
                        setApiError={setApiError}
                    />
                </DialogContent>
            </Dialog>

            {/* Snackbar pour les notifications */}
            <Snackbar
                open={openSnackbar}
                autoHideDuration={3000}
                onClose={() => setOpenSnackbar(false)}
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
            >
                <Alert onClose={() => setOpenSnackbar(false)} severity="success" sx={{ width: '100%' }}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </ThemeProvider>
    );
};

export default GroupCardList;