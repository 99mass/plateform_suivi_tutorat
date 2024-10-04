import React, { useState, useEffect, useCallback } from 'react';
import {
    Box,
    TextField,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    CircularProgress,
} from '@mui/material';
import ModuleService from '../../services/moduleService';

const GroupForm = ({ initialData, onSubmit, onClose, apiError, setApiError }) => {
    const [formData, setFormData] = useState({
        id: '',
        nom: '',
        module: { id: '' }
    });
    const [modules, setModules] = useState([]);
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});

    const fetchModules = useCallback(async () => {
        try {
            const modulesData = await ModuleService.getAllModules();
            setModules(modulesData);
        } catch (error) {
            console.error("Erreur lors de la récupération des modules:", error);
        }
    }, []);

    useEffect(() => {
        fetchModules();
        if (initialData) {
            setFormData({
                id: initialData?.id,
                nom: initialData?.nom,
                module: { id: initialData?.module?.id || '' }
            });
        }
    }, [initialData, fetchModules]);

    const validateForm = () => {
        const newErrors = {};
        if (!formData.nom.trim()) newErrors.nom = 'Le nom du groupe est requis';
        if (!formData.module.id) newErrors.moduleId = 'Le module est requis';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (validateForm()) {
            setLoading(true);
            setApiError('');
            try {
                await onSubmit(formData);
                setLoading(false);
            } catch (error) {
                console.error("Erreur lors de l'ajout/modification du groupe:", error);
                setApiError(error.response?.data?.message || "Erreur, veuillez réessayer.");
                setLoading(false);
            }
        }
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        if (name === 'moduleId') {
            setFormData(prevData => ({
                ...prevData,
                module: { id: value }
            }));
        } else {
            setFormData(prevData => ({
                ...prevData,
                [name]: value
            }));
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 2 }}>
            <TextField
                margin="normal"
                required
                fullWidth
                id="nom"
                label="Nom du groupe"
                name="nom"
                value={formData.nom}
                onChange={handleChange}
                error={!!errors.nom}
                helperText={errors.nom}
                disabled={loading}
            />
            <FormControl fullWidth margin="normal" error={!!errors.moduleId}>
                <InputLabel id="module-label">Module</InputLabel>
                {modules.length > 0 ? (
                    <Select
                        labelId="module-label"
                        id="moduleId"
                        name="moduleId"
                        value={formData.module.id || ''}  
                        label="Module"
                        onChange={handleChange}
                        disabled={loading}
                    >
                        {modules.map((module) => (
                            <MenuItem key={module.id} value={module.id}>
                                {module.nom}
                            </MenuItem>
                        ))}
                    </Select>
                ) : (
                    <CircularProgress size={24} />
                )}
            </FormControl>
            {apiError && <p style={{ color: 'red' }}>{apiError}</p>}
            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 3 }}>
                <Button onClick={onClose} sx={{ mr: 2 }} disabled={loading}>
                    Annuler
                </Button>
                <Button type="submit" variant="contained" disabled={loading}>
                    {loading ? <CircularProgress size={24} /> : (initialData ? 'Modifier' : 'Ajouter')}
                </Button>
            </Box>
        </Box>
    );
};

export default GroupForm;