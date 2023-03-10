import os
import shutil

input_directory = 'input_tests'
output_directory = 'expected_output_tests'
test_parent = 'Tests'
BASE_PATH = os.path.dirname(os.path.realpath(__file__))

if not (os.path.exists(input_directory) and os.path.exists(output_directory)):
    os.umask(0)
    os.mkdir(input_directory)
    os.mkdir(output_directory)

    #loop over each directory
    for filename in os.listdir(test_parent):
        function = os.path.join(test_parent, filename)
        for fname in os.listdir(function):
            test_folder = os.path.join(function, fname)
            for test in os.listdir(test_folder):
                if(test.endswith('.inp')):
                    shutil.copy(os.path.join(test_folder, test), os.path.join(BASE_PATH, input_directory))
                elif(test.endswith('.out')):
                    shutil.copy(os.path.join(test_folder, test), os.path.join(BASE_PATH, output_directory))
                    
            
        


    




