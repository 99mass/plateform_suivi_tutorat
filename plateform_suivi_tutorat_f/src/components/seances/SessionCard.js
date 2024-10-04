import React from 'react';
import {
    Typography,
    Box,
    Card,
    CardContent,
    Button,
    Checkbox,
    FormControlLabel,
    Divider,
    Tooltip,
} from '@mui/material';

import {
    Person as PersonIcon,
    Class as ClassIcon,
    Group as GroupIcon,
    CalendarToday as CalendarIcon,
    AccessTime as AccessTimeIcon,
} from '@mui/icons-material';
import theme from '../../utils/theme';



const SessionCard = ({ session, isSelected, onSelect, onMarkSession }) => {

    const weekCount = session.module.nombreSemaines || 0;
    const totalHours = session.module.nombreSemaines * 2 || 0;
    const heuresEffectuees = session.heuresEffectuees || 0;
    const heuresNonEffectuees = session.heuresNonEffectuees || 0;
    const remainingHours = totalHours - heuresEffectuees - heuresNonEffectuees;

    return (
        <Card
            elevation={2}
            sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                overflow: 'visible',
                border: isSelected ? '2px solid #1976d2' : 'none',
            }}
        >
            <CardContent sx={{ flexGrow: 1, position: 'relative', pt: 1 }}>
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={isSelected}
                            onChange={() => onSelect(session.id)}
                            color="primary"
                        />
                    }
                    label=""
                    sx={{ position: 'absolute', top: 0, right: 0 }}
                />
                <Box sx={{ display: 'flex', alignItems: 'center', pr: 4, color: theme.palette.primary }}>
                    <ClassIcon sx={{ fontSize: 30, flexShrink: 0, mr: 1, color: theme.palette.primary.main }} />
                    <Tooltip title={session.module.nom || 'N/A'} placement="top-start">
                        <Typography
                            variant="h6"
                            component="div"
                            fontWeight='500'
                            sx={{
                                maxWidth: 'calc(100% - 40px)',
                                whiteSpace: 'nowrap',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                            }}
                        >
                            {session.module?.nom || 'N/A'}
                        </Typography>
                    </Tooltip>
                </Box>
                <Divider sx={{ my: 2 }} />

                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <PersonIcon sx={{ fontSize: 25, mr: 1, color: 'text.secondary' }} />
                    <Typography variant="body2" color="text.secondary">
                        {`${session.tuteur?.prenom || 'N/A'} ${session.tuteur?.nom || 'N/A'}`}
                    </Typography>
                </Box>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <GroupIcon sx={{ fontSize: 25, mr: 1, color: 'text.secondary' }} />
                    <Typography variant="body2" color="text.secondary">
                        {session.groupe?.nom || 'N/A'}
                    </Typography>
                </Box>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <CalendarIcon sx={{ fontSize: 25, mr: 1, color: 'text.secondary' }} />
                    <Typography variant="body2" color="text.secondary">
                        {session.date ? new Date(session.date).toLocaleDateString() : 'N/A'}
                    </Typography>
                </Box>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <AccessTimeIcon sx={{ fontSize: 25, mr: 1, color: 'text.secondary' }} />
                    <Typography variant="body2" color="text.secondary">
                        Semaines: {weekCount} | Total: {totalHours}h
                    </Typography>
                </Box>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
                    <Typography variant="body2" color="success.main">
                        Effectuées: {heuresEffectuees}h
                    </Typography>
                    <Typography variant="body2" color="error.main">
                        Non effectuées: {heuresNonEffectuees}h
                    </Typography>
                </Box>
                <Typography variant="body2" color="info.main" sx={{ mt: 1 }}>
                    Restantes: {remainingHours}h
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ mt: 2 }}
                    onClick={() => onMarkSession(session.id)}
                    disabled={remainingHours === 0}
                >
                    Marquer la séance
                </Button>
            </CardContent>
        </Card>
    );
};

export default SessionCard;