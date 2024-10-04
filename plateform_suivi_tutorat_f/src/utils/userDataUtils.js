export const generateUsers = (type, count) => {
    return Array.from({ length: count }, (_, index) => ({
      id: index + 1,
      nom: `Nom${index + 1}`,
      prenom: `Prenom${index + 1}`,
      email: `${type.toLowerCase()}${index + 1}@example.com`,
      telephone: `0${Math.floor(Math.random() * 900000000 + 100000000)}`,
      role: type
    }));
  };
  