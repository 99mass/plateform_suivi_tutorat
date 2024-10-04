// src/pages/BlocCadre.js
import React, { useEffect, useState } from 'react';
import { Card, CardContent, Typography, Grid, Box, LinearProgress, useTheme } from '@mui/material';
import PersonIcon from '@mui/icons-material/Person';
import SchoolIcon from '@mui/icons-material/School';
import GroupIcon from '@mui/icons-material/Group';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import PercentIcon from '@mui/icons-material/Percent';
import TrackChangesIcon from '@mui/icons-material/TrackChanges';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import { Dashboard } from '@mui/icons-material';
import UserService from '../../services/userService';
import ModuleService from '../../services/moduleService';
import GroupeService from '../../services/groupeService';
import sessionService from '../../services/sessionService';
import SeanceService from '../../services/seancesService';

const BlocCadre = () => {
    const theme = useTheme();

    const [user, setUser] = useState(null);
    const [countAdmin, setCountAdmin] = useState(0);
    const [countTracker, setCountTracker] = useState(0);
    const [countTuteur, setCountTuteur] = useState(0);
    const [countModule, setCountModule] = useState(0);
    const [countGroupe, setCountGroupe] = useState(0);
    const [hoursCompleted, setHoursCompleted] = useState(0);
    const [hoursNotCompleted, setHoursNotCompleted] = useState(0);
    const [completionPercentage, setCompletionPercentage] = useState(0);

    useEffect(() => {
        const fetchData = async () => {
            const infos = await sessionService.userInformation();
            setUser(infos);

            const fetchedSeances = await SeanceService.getAllSeances();

            const fetchedAdmin = await UserService.listUsers("Administrateur");
            setCountAdmin(fetchedAdmin.length);
            const fetchedTracker = await UserService.listUsers("Tracker");
            setCountTracker(fetchedTracker.length);
            const fetchedTuteur = await UserService.listUsers("tuteur");
            setCountTuteur(fetchedTuteur.length);
            console.log(fetchedAdmin);
            
            const fetchedModule = await ModuleService.getAllModules();
            setCountModule(fetchedModule.length);
            const fetchedGroupe = await GroupeService.getAllGroupes();
            setCountGroupe(fetchedGroupe.length);

            let totalCompleted = 0;
            let totalNotCompleted = 0;
            let totalHours = 0;

            fetchedSeances.forEach(seance => {
                totalCompleted += seance.heuresEffectuees;
                totalNotCompleted += seance.heuresNonEffectuees;
                totalHours += seance.module.nombreSemaines * 2; 
            });

            setHoursCompleted(totalCompleted);
            setHoursNotCompleted(totalNotCompleted);
            setCompletionPercentage(Math.round((totalCompleted / totalHours) * 100));
        };

        fetchData();
    }, []);

    const cardData = [
        user && user.role === "admin" && { title: 'Admins', count: countAdmin, icon: AdminPanelSettingsIcon, color: 'rgba(21, 62, 80)' },
        user && user.role === "admin" && { title: 'Trackers', count: countTracker, icon: TrackChangesIcon, color: theme.palette.primary.main },
        { title: 'Tuteurs', count: countTuteur, icon: PersonIcon, color: theme.palette.purple },
        { title: 'Modules', count: countModule, icon: SchoolIcon, color: theme.palette.secondary.main },
        { title: 'Groupes', count: countGroupe, icon: GroupIcon, color: theme.palette.success.main },
        { title: 'H. Effectuées', count: hoursCompleted, icon: CheckCircleIcon, color: theme.palette.info.main },
        { title: 'H. Manquées', count: hoursNotCompleted, icon: CancelIcon, color: theme.palette.error.main },
        { title: 'Complétion', count: `${completionPercentage}%`, icon: PercentIcon, color: theme.palette.warning.main },
    ].filter(Boolean);

    return (
        <Box sx={{ flexGrow: 1, px: 3, backgroundColor: 'transparent' }} className='bloc-card' >
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 4 }}>
                <Dashboard sx={{ color: 'primary.main', fontSize: 40 }} />
                <Typography variant="h4" sx={{ fontWeight: 'bold', color: 'primary.main' }}>
                    Tableau de Bord
                </Typography>
            </Box>
            <Grid container spacing={2}>
                {cardData.map((item, index) => (
                    <Grid item xs={12} sm={6} md={4} key={index}>
                        <Card
                            sx={{
                                height: '100%',
                                display: 'flex',
                                flexDirection: 'column',
                                transition: 'transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out',
                                '&:hover': {
                                    transform: 'translateY(-5px)',
                                    boxShadow: theme.shadows[10],
                                },
                            }}
                        >
                            <CardContent sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                                <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                                    <Typography variant="h6" component="div" color="text.secondary" sx={{ fontWeight: 'medium' }}>
                                        {item.title}
                                    </Typography>
                                    <Box sx={{ color: item.color }}>
                                        <item.icon sx={{ fontSize: 40 }} />
                                    </Box>
                                </Box>
                                <Typography variant="h4" component="div" sx={{ fontWeight: 'bold', color: item.color }}>
                                    {item.count}
                                </Typography>
                                {item.title === 'Complétion' && (
                                    <Box sx={{ width: '100%', mt: 2 }}>
                                        <LinearProgress
                                            variant="determinate"
                                            value={completionPercentage}
                                            sx={{
                                                height: 10,
                                                borderRadius: 5,
                                                backgroundColor: theme.palette.grey[200],
                                                '& .MuiLinearProgress-bar': {
                                                    backgroundColor: item.color,
                                                }
                                            }}
                                        />
                                    </Box>
                                )}
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
};

export default BlocCadre;