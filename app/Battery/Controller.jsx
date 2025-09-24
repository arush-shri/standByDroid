import { forwardRef, useImperativeHandle } from "react";
import { View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";

export const Controller = forwardRef(( {}, ref ) => {
    const handleTripleTap = () => {
        console.log('b controller')
    }
    useImperativeHandle(ref, () => ({
        handleTripleTap
    }));
    return(
        <View style={styles.container}>

        </View>
    )
});

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'red'
    },
})