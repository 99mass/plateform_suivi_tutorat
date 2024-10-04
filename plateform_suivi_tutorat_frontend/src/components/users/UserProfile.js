import React, { useState, useEffect } from 'react';
import {
    Box,
    TextField,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Typography,
    Paper,
    Container,
    Avatar,
    Grid,
    IconButton,
    Tooltip,
    Snackbar,
    Alert,
} from '@mui/material';
import {
    Edit as EditIcon,
    Person as PersonIcon,
    Email as EmailIcon,
    Phone as PhoneIcon,
    Work as WorkIcon,
    Save as SaveIcon,
    Cancel as CancelIcon,
    Lock as LockIcon,
} from '@mui/icons-material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import AuthService from '../../services/authService';

const theme = createTheme({
    palette: {
        primary: {
            main: '#1976d2',
        },
        secondary: {
            main: '#dc004e',
        },
    },
});

const UserProfile = ({ userData: initialUserData }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [isEditingRole, setIsEditingRole] = useState(false);
    const [formData, setFormData] = useState({
        nom: '',
        prenom: '',
        email: '',
        telephone: '',
        role: '',
    });
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (initialUserData) {
            setFormData({
                nom: initialUserData.nom || '',
                prenom: initialUserData.prenom || '',
                email: initialUserData.email || '',
                telephone: initialUserData.telephone || '',
                role: initialUserData.role || '',
            });
        }
    }, [initialUserData]);

    const handleChangeEdition = () => {
        if (formData.role === "admin") {
            setIsEditingRole(true);
        }
        setIsEditing(true);
    };

    const handleChangeEdition2 = () => {
        if (formData.role === "admin") {
            setIsEditingRole(false);
        }
        setIsEditing(false);
        setOldPassword('');
        setNewPassword('');
        setErrors({});
        if (initialUserData) {
            setFormData({
                nom: initialUserData.nom || '',
                prenom: initialUserData.prenom || '',
                email: initialUserData.email || '',
                telephone: initialUserData.telephone || '',
                role: initialUserData.role || '',
            });
        }
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData(prevData => ({
            ...prevData,
            [name]: value
        }));
        setErrors(prevErrors => ({ ...prevErrors, [name]: '' }));
    };

    const validateForm = () => {
        const newErrors = {};
        if (!formData.nom || formData.nom.trim() === "") newErrors.nom = 'Le nom est requis';
        if (!formData.prenom || formData.prenom.trim() === "") newErrors.prenom = 'Le prénom est requis';
        if (!formData.email || formData.email.trim() === "") newErrors.email = 'L\'email est requis';
        if (!/\S+@\S+\.\S+/.test(formData.email)) newErrors.email = 'L\'email est invalide';
        if (!formData.role || formData.role.trim() === "") newErrors.role = 'Le rôle est requis';

        if (newPassword && !oldPassword) {
            newErrors.oldPassword = 'L\'ancien mot de passe est requis pour changer le mot de passe';
        }
        if (oldPassword && !newPassword) {
            newErrors.newPassword = 'Le nouveau mot de passe est requis';
        }
        if (oldPassword && oldPassword && newPassword.trim() === "" && oldPassword.trim() === "") newErrors.role = 'L\'ancien et le nouveau mot de passe est requis pour changer le mot de passe';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!validateForm()) {
            setSnackbar({ open: true, message: 'Veuillez corriger les erreurs dans le formulaire', severity: 'error' });
            return;
        }

        try {
            const dataToUpdate = {
                nom: formData.nom,
                prenom: formData.prenom,
                email: formData.email,
                telephone: formData.telephone,
                role: initialUserData.role === "admin" ? formData.role : initialUserData.role,
                oldPassword: oldPassword,
                newPassword: newPassword,
            };



            await AuthService.UpdateUser(dataToUpdate, initialUserData.id);

            setSnackbar({ open: true, message: 'Profile mis à jour avec succès!', severity: 'success' });
            setIsEditing(false);
            setOldPassword('');
            setNewPassword('');
        } catch (error) {
            setSnackbar({ open: true, message: 'Erreur lors de la mise à jour du profil. Veuillez réessayer.', severity: 'error' });
        }
    };

    const handleCloseSnackbar = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setSnackbar({ ...snackbar, open: false });
    };

    return (
        <ThemeProvider theme={theme}>
            {initialUserData !== null && (
                <Container component="main" maxWidth="md">
                    <Paper elevation={3} sx={{ mt: 8, mb: 8, overflow: 'hidden' }}>
                        <Box sx={{
                            bgcolor: 'primary.main',
                            color: 'white',
                            p: 3,
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between'
                        }}>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                <Avatar sx={{ width: 80, height: 80, mr: 2, bgcolor: 'secondary' }}>
                                    <PersonIcon sx={{ fontSize: 40 }} />
                                </Avatar>
                                <Box>
                                    <Typography variant="h4">
                                        {formData.prenom.charAt(0).toUpperCase() + formData.prenom.slice(1).toLowerCase()} {formData.nom.charAt(0).toUpperCase() + formData.nom.slice(1).toLowerCase()}
                                    </Typography>
                                    <Typography variant="subtitle1" sx={{ fontWeight: 'bold', fontStyle: 'italic' }}>
                                        {formData.role.charAt(0).toUpperCase() + formData.role.slice(1)}
                                    </Typography>
                                </Box>
                            </Box>
                            <Tooltip title={!isEditing ? "Modifier" : "Enregistrer"}>
                                <IconButton
                                    onClick={!isEditing ? handleChangeEdition : handleSubmit}
                                    sx={{ color: 'white' }}
                                >
                                    {isEditing ? <SaveIcon /> : <EditIcon />}
                                </IconButton>
                            </Tooltip>
                        </Box>
                        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ p: 4 }}>
                            <Grid container spacing={3}>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        required
                                        fullWidth
                                        id="nom"
                                        label="Nom"
                                        name="nom"
                                        value={formData.nom}
                                        onChange={handleChange}
                                        disabled={!isEditing}
                                        error={!!errors.nom}
                                        helperText={errors.nom}
                                        InputProps={{
                                            startAdornment: <PersonIcon color="action" sx={{ mr: 1 }} />,
                                        }}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        required
                                        fullWidth
                                        id="prenom"
                                        label="Prénom"
                                        name="prenom"
                                        value={formData.prenom}
                                        onChange={handleChange}
                                        disabled={!isEditing}
                                        error={!!errors.prenom}
                                        helperText={errors.prenom}
                                        InputProps={{
                                            startAdornment: <PersonIcon color="action" sx={{ mr: 1 }} />,
                                        }}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        fullWidth
                                        id="email"
                                        label="Adresse email"
                                        name="email"
                                        value={formData.email}
                                        onChange={handleChange}
                                        disabled={!isEditing}
                                        error={!!errors.email}
                                        helperText={errors.email}
                                        InputProps={{
                                            startAdornment: <EmailIcon color="action" sx={{ mr: 1 }} />,
                                        }}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        fullWidth
                                        id="telephone"
                                        label="Téléphone"
                                        name="telephone"
                                        value={formData.telephone}
                                        onChange={handleChange}
                                        disabled={!isEditing}
                                        InputProps={{
                                            startAdornment: <PhoneIcon color="action" sx={{ mr: 1 }} />,
                                        }}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <FormControl fullWidth error={!!errors.role}>
                                        <InputLabel id="role-label">Rôle</InputLabel>
                                        <Select
                                            labelId="role-label"
                                            id="role"
                                            name="role"
                                            value={formData.role}
                                            label="Rôle"
                                            onChange={handleChange}
                                            disabled={!isEditingRole}
                                            startAdornment={<WorkIcon color="action" sx={{ mr: 1, ml: -0.5 }} />}
                                        >
                                            <MenuItem value="admin">Admin</MenuItem>
                                            <MenuItem value="tracker">Tracker</MenuItem>
                                            <MenuItem value="tuteur">Tuteur</MenuItem>
                                        </Select>
                                        {errors.role && <Typography color="error" variant="caption">{errors.role}</Typography>}
                                    </FormControl>
                                </Grid>
                                {isEditing && (
                                    <>
                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                fullWidth
                                                id="oldPassword"
                                                label="Ancien mot de passe"
                                                name="oldPassword"
                                                type="password"
                                                value={oldPassword}
                                                onChange={(e) => setOldPassword(e.target.value)}
                                                error={!!errors.oldPassword}
                                                helperText={errors.oldPassword}
                                                InputProps={{
                                                    startAdornment: <LockIcon color="action" sx={{ mr: 1 }} />,
                                                }}
                                            />
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                fullWidth
                                                id="newPassword"
                                                label="Nouveau mot de passe"
                                                name="newPassword"
                                                type="password"
                                                value={newPassword}
                                                onChange={(e) => setNewPassword(e.target.value)}
                                                error={!!errors.newPassword}
                                                helperText={errors.newPassword}
                                                InputProps={{
                                                    startAdornment: <LockIcon color="action" sx={{ mr: 1 }} />,
                                                }}
                                            />
                                        </Grid>
                                    </>
                                )}
                            </Grid>
                            {isEditing && (
                                <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 3 }}>
                                    <Button
                                        variant="outlined"
                                        onClick={handleChangeEdition2}
                                        startIcon={<CancelIcon />}
                                        sx={{ mr: 2 }}
                                    >
                                        Annuler
                                    </Button>
                                    <Button
                                        type="submit"
                                        variant="contained"
                                        startIcon={<SaveIcon />}
                                    >
                                        Enregistrer
                                    </Button>
                                </Box>
                            )}
                        </Box>
                    </Paper>
                </Container>
            )}
            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
            >
                <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </ThemeProvider>
    );
};

export default UserProfile;