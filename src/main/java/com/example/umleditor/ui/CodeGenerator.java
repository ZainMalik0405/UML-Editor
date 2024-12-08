package com.example.umleditor.ui;

import com.example.umleditor.ui.components.ClassComponent;
import com.example.umleditor.ui.components.ClassDiagramConnection;
import com.example.umleditor.ui.components.ArrowType;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CodeGenerator {

    public static void generateCode(List<ClassComponent> classes) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Destination Folder");
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            System.out.println("Selected directory: " + selectedDirectory.getAbsolutePath());
            for (ClassComponent classComponent : classes) {
                String className = classComponent.getName();
                StringBuilder classCode = new StringBuilder();

                // Handle inheritance (generalization)
                boolean hasSuperclass = false;
                boolean i=false;
                String superclassName = "";
                for (ClassDiagramConnection connection : classComponent.getConnections()) {
                    if (ArrowType.GENERALIZATION.equals(connection.getType()) && connection.getStart().equals(classComponent)) {
                        hasSuperclass = true;
                        superclassName = connection.getEnd().getName();
                        i=connection.getEnd().isInterface();
                        break;
                    }
                }

                // Populate class or interface template
                if (!classComponent.isInterface()) {
                    classCode.append("public class ").append(className);
                    if (hasSuperclass) {
                        if(i==false)
                        {
                            classCode.append(" extends ").append(superclassName);
                        }
                        else {
                            classCode.append(" implements ").append(superclassName);
                        }
                    }
                    classCode.append(" {\n");
                } else {
                    classCode.append("public interface ").append(className).append(" {\n");
                }

                // Add attributes
                for (String attribute : classComponent.getAttributes()) {
                    String[] parts = attribute.split(" ");
                    if (parts.length == 2) {
                        classCode.append("    private ").append(parts[0]).append(" ").append(parts[1]).append(";\n");
                    }
                }

                // Add methods
                for (String method : classComponent.getMethods()) {
                    String[] parts = method.split(" ");
                    if (parts.length >= 2) {
                        classCode.append("    public ").append(parts[0]).append(" ").append(parts[1]).append(" {\n");
                        classCode.append("        // TODO: Implement method\n");
                        classCode.append("    }\n");
                    }
                }

                // Handle aggregation and composition
                for (ClassDiagramConnection connection : classComponent.getConnections()) {
                    if ((ArrowType.AGGREGATION.equals(connection.getType()) || ArrowType.COMPOSITION.equals(connection.getType())) && connection.getStart().equals(classComponent)) {
                        String endClassName = connection.getEnd().getName();
                        String multiplicity = connection.getEndMultiplicity();
                        int x = Integer.parseInt(multiplicity);
                        classCode.append("    private ").append(" ").append(x > 1 ? "List<"+endClassName+"> "+endClassName.toLowerCase()+(";\n")  : endClassName.toLowerCase()).append(";\n");
                    }
                }

                classCode.append("}\n");

                // Write to file
                File outputFile = new File(selectedDirectory, className + ".java");
                System.out.println("Writing to file: " + outputFile.getAbsolutePath());
                try (FileWriter fileWriter = new FileWriter(outputFile)) {
                    fileWriter.write(classCode.toString());
                    System.out.println("Successfully wrote to file: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Failed to write to file: " + outputFile.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No directory selected.");
        }
    }
}