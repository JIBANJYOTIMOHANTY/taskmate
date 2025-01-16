import { StyleSheet, Text, View } from 'react-native'
import React from 'react'
import { NavigationContainer } from '@react-navigation/native'
import { createNativeStackNavigator } from '@react-navigation/native-stack'
import LoginScreen from './screens/LoginScreen'
import SignupScreen from './screens/SignupScreen'
import TaskScreen from './screens/TaskScreen'

export type RootStackParamList = {
  LoginScreen: undefined;
  SignupScreen: undefined;
  TaskScreen: undefined;
}

const App = () => {
  const Stack = createNativeStackNavigator<RootStackParamList>();
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="LoginScreen" screenOptions={{headerShown : false}}>
        <Stack.Screen name="LoginScreen" component={LoginScreen} options={{title : 'Products'}}/>
        <Stack.Screen name="SignupScreen" component={SignupScreen} options={{title : 'Sign'}}/>
        <Stack.Screen name="TaskScreen" component={TaskScreen} options={{title : 'Task'}}/>
      </Stack.Navigator>
    </NavigationContainer>
  )
}

export default App

const styles = StyleSheet.create({})