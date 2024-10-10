import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, Button, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { initializeApp } from '@firebase/app';
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword, onAuthStateChanged, signOut, updatePassword, updateProfile } from '@firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyDEvN1Iayz3OQgfS4uIxCbi_qdsoL82n_M",
  authDomain: "desafio3-4a98c.firebaseapp.com",
  projectId: "desafio3-4a98c",
  storageBucket: "desafio3-4a98c.appspot.com",
  messagingSenderId: "532551486638",
  appId: "1:532551486638:web:e9c6e8467de38082d4713f"
};

const app = initializeApp(firebaseConfig);

const ProfileMenu = ({ handleSignOut }) => {
  const auth = getAuth();

  const [newPassword, setNewPassword] = useState('');
  const [newPhotoURL, setNewPhotoURL] = useState('');

  const handleChangePassword = async () => {
    try {
      await updatePassword(auth.currentUser, newPassword);
      console.log('Contraseña cambiada exitosamente');
    } catch (error) {
      console.error('Error al cambiar la contraseña:', error.message);
    }
  };

  const handleChangePhotoURL = async () => {
    try {
      await updateProfile(auth.currentUser, { photoURL: newPhotoURL });
      console.log('Foto de perfil cambiada exitosamente');
    } catch (error) {
      console.error('Error al cambiar la foto de perfil:', error.message);
    }
  };

  return (
    <View style={styles.profileMenu}>
      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          value={newPassword}
          onChangeText={setNewPassword}
          placeholder="Nueva contraseña"
          secureTextEntry
        />
        <TouchableOpacity style={styles.button} onPress={handleChangePassword}>
          <Text style={styles.buttonText}>Cambiar</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          value={newPhotoURL}
          onChangeText={setNewPhotoURL}
          placeholder="URL nueva foto de perfil"
        />
        <TouchableOpacity style={styles.button} onPress={handleChangePhotoURL}>
          <Text style={styles.buttonText}>Cambiar</Text>
        </TouchableOpacity>
      </View>

      <TouchableOpacity style={styles.signOutButton} onPress={handleSignOut}>
        <Text style={styles.signOutButtonText}>Cerrar sesión</Text>
      </TouchableOpacity>
    </View>
  );
};

const AuthScreen = ({ email, setEmail, password, setPassword, isLogin, setIsLogin, handleAuthentication }) => {
  return (
    <View style={styles.authContainer}>
       <Text style={styles.title}>{isLogin ? 'Iniciar sesión' : 'Registrarse'}</Text>

       <TextInput
        style={styles.input}
        value={email}
        onChangeText={setEmail}
        placeholder="Correo"
        autoCapitalize="none"
      />
      <TextInput
        style={styles.input}
        value={password}
        onChangeText={setPassword}
        placeholder="Contraseña"
        secureTextEntry
      />
      <View style={styles.buttonContainer}>
        <Button title={isLogin ? 'Iniciar sesión' : 'Registrarse'} onPress={handleAuthentication} color="#3498db" />
      </View>

      <View style={styles.bottomContainer}>
        <Text style={styles.toggleText} onPress={() => setIsLogin(!isLogin)}>
          {isLogin ? '¿Necesitas una cuenta? Regístrate' : '¿Ya tienes una cuenta? Inicia sesión'}
        </Text>
      </View>
    </View>
  );
}

const App = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [user, setUser] = useState(null); // Track user authentication state
  const [isLogin, setIsLogin] = useState(true);
  const [menuVisible, setMenuVisible] = useState(false);

  const auth = getAuth();
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setUser(user);
    });

    return () => unsubscribe();
  }, [auth]);

  const handleAuthentication = async () => {
    try {
      if (user) {
        // If user is already authenticated, log out
        console.log('User logged out successfully!');
        await signOut(auth);
      } else {
        // Sign in or sign up
        if (isLogin) {
          // Sign in
          await signInWithEmailAndPassword(auth, email, password);
          console.log('User signed in successfully!');
        } else {
          // Sign up
          await createUserWithEmailAndPassword(auth, email, password);
          console.log('User created successfully!');
        }
      }
    } catch (error) {
      console.error('Authentication error:', error.message);
    }
  };

  const handleSignOut = async () => {
    try {
      await signOut(auth);
      console.log('User signed out successfully!');
    } catch (error) {
      console.error('Sign out error:', error.message);
    }
  };

  const toggleMenu = () => {
    setMenuVisible(!menuVisible);
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      {user ? (
        // Show user's email if user is authenticated
        <>
          <Text style={styles.emailText}>Bienvenido, {user.email}</Text>
          <TouchableOpacity style={styles.menuButton} onPress={toggleMenu}>
            <Text style={styles.menuText}>☰</Text>
          </TouchableOpacity>
          {menuVisible && (
            <ProfileMenu handleSignOut={handleSignOut} />
          )}
        </>
      ) : (
        // Show sign-in or sign-up form if user is not authenticated
        <View style={styles.centeredContainer}>
          <AuthScreen
            email={email}
            setEmail={setEmail}
            password={password}
            setPassword={setPassword}
            isLogin={isLogin}
            setIsLogin={setIsLogin}
            handleAuthentication={handleAuthentication}
          />
        </View>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    backgroundColor: '#f0f0f0',
  },
  authContainer: {
    width: '80%',
    maxWidth: 400,
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 8,
    elevation: 3,
    marginTop: 40,
    marginLeft: 20,
  },
  centeredContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 24,
    marginBottom: 16,
    textAlign: 'center',
  },
  input: {
    height: 40,
    borderColor: '#ddd',
    borderWidth: 1,
    marginBottom: 16,
    padding: 8,
    borderRadius: 4,
  },
  buttonContainer: {
    marginBottom: 16,
  },
  toggleText: {
    color: '#3498db',
    textAlign: 'center',
  },
  bottomContainer: {
    marginTop: 20,
  },
  emailText: {
    fontSize: 18,
    textAlign: 'center',
    marginTop: 20,
  },
  profileMenu: {
    backgroundColor: '#fff',
    padding: 16,
    borderRadius: 8,
    elevation: 4,
    marginHorizontal: 20,
    marginTop: 40,
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  button: {
    backgroundColor: '#3498db',
    borderRadius: 4,
    paddingVertical: 8,
    paddingHorizontal: 12,
  },
  buttonText: {
    color: '#fff',
    textAlign: 'center',
  },
  signOutButton: {
    backgroundColor: '#e74c3c',
    borderRadius: 4,
    paddingVertical: 12,
  },
  signOutButtonText: {
    color: '#fff',
    textAlign: 'center',
  },
  menuButton: {
    position: 'absolute',
    top: 0,
    right: 0,
    padding: 16,
    zIndex: 1,
  },
  menuText: {
    color: '#000',
    fontSize: 24,
  },
});


export default App;
