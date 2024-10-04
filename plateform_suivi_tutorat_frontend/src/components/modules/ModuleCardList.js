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
import { Edit, Delete, Search, Add, ViewModule } from '@mui/icons-material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import ClassIcon from '@mui/icons-material/Class';
import ModuleForm from './ModuleForm';
import ModuleService from '../../services/moduleService';

const theme = createTheme({
    palette: {
        primary: { main: '#1976d2' },
        secondary: { main: '#dc004e' },
    },
});

const ModuleCardList = () => {
    const [modules, setModules] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [editModule, setEditModule] = useState(null);
    const [deleteModule, setDeleteModule] = useState(null);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [openAddModal, setOpenAddModal] = useState(false);
    const [apiError, setApiError] = useState('');

    useEffect(() => {
        fetchModules();
    }, []);

    const fetchModules = async () => {
        try {
            const modulesData = await ModuleService.getAllModules();
            setModules(modulesData);
        } catch (error) {
            console.error("Erreur lors de la récupération des modules:", error);
        }
    };

    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    const filteredModules = modules.filter(module =>
        module.nom.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleEdit = (module) => {
        setEditModule(module);
    };

    const handleDelete = (module) => {
        setDeleteModule(module);
    };

    const handleCloseEdit = () => {
        setEditModule(null);
    };

    const handleCloseDelete = () => {
        setDeleteModule(null);
    };

    const handleSaveEdit = async (editedModule) => {
        try {
            await ModuleService.UpdateModules(editedModule, editedModule.id);
            setModules(modules.map(m => m.id === editedModule.id ? editedModule : m));
            setEditModule(null);
            setSnackbarMessage('Module modifié avec succès');
            setOpenSnackbar(true);
        } catch (error) {
            setApiError(error.response?.data?.message || "Erreur lors de la modification. Veuillez réessayer.");
            console.error("Erreur lors de la modification:", error);
        }
    };

    const handleConfirmDelete = async () => {
        try {
            await ModuleService.deleteModule(deleteModule.id);
            setModules(modules.filter(m => m.id !== deleteModule.id));
            setDeleteModule(null);
            setSnackbarMessage('Module supprimé avec succès');
            setOpenSnackbar(true);
        } catch (error) {
            setApiError(error.response?.data?.message || "Erreur lors de la suppression. Veuillez réessayer.");
            console.error("Erreur lors de la suppression:", error);
        }
    };

    const handleAddModule = async (newModule) => {
        try {
            await ModuleService.createModule(newModule);
            fetchModules();
            setOpenAddModal(false);
            setSnackbarMessage('Module ajouté avec succès');
            setOpenSnackbar(true);
        } catch (error) {
            console.error("Erreur lors de l'ajout du module:", error);
            setApiError(error.response?.data?.message || "Erreur lors de l'ajout du module. Veuillez réessayer.");
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Box sx={{ p: 3 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 4 }}>
                        <ViewModule sx={{ color: 'primary.main', fontSize: 40 }} />
                        <Typography variant="h4" sx={{ fontSize: 30, fontWeight: 'bold', color: 'primary.main' }}>
                            Gestion des Modules
                        </Typography>
                    </Box>
                </Box>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        placeholder="Rechercher un module..."
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
                        Ajouter un module
                    </Button>
                </Box>
                <Divider sx={{ mb: 3 }} />
                <Grid container spacing={2}>
                    {filteredModules.map(module => (
                        <Grid item xs={12} sm={6} md={4} key={module.id}>
                            <Card elevation={2} sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                                <CardContent sx={{ flexGrow: 1 }}>
                                    <Typography variant="h6" component="div" gutterBottom>
                                        {module.nom}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Durée: {module.nombreSemaines} semaine{module.nombreSemaines > 1 ? 's' : ''}
                                    </Typography>
                                </CardContent>
                                <Box sx={{ display: 'flex', justifyContent: 'flex-end', p: 1 }}>
                                    <IconButton onClick={() => handleEdit(module)} aria-label="modifier" sx={{ color: '#3B5998' }}>
                                        <Edit />
                                    </IconButton>
                                    <IconButton onClick={() => handleDelete(module)} aria-label="supprimer" sx={{ color: 'rgb(143, 16, 16)' }} >
                                        <Delete />
                                    </IconButton>
                                </Box>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            </Box>

            {/* Dialog pour l'édition */}
            <Dialog open={!!editModule} onClose={handleCloseEdit}>
                <DialogTitle>Modifier le module</DialogTitle>
                <DialogContent>
                    <ModuleForm
                        initialData={editModule}
                        handleSaveEdit={handleSaveEdit}
                        onClose={handleCloseEdit}
                        apiError={apiError}
                        setApiError={setApiError}
                    />
                </DialogContent>
            </Dialog>

            {/* Dialog pour la suppression */}
            <Dialog
                open={!!deleteModule}
                onClose={handleCloseDelete}
            >
                <DialogTitle>Confirmer la suppression</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Êtes-vous sûr de vouloir supprimer le module "{deleteModule?.nom}" ?
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
                    <ClassIcon sx={{ fontSize: 40, mr: 1, color: 'primary.main' }} />
                    <Typography component="h2" variant="h5" fontWeight='bold'>
                        Ajouter un nouveau module
                    </Typography>
                </DialogTitle>
                <DialogContent>
                    <ModuleForm
                        initialData={null}
                        onClose={() => setOpenAddModal(false)}
                        handleAddModule={handleAddModule}
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

export default ModuleCardList;
