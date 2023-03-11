# there are two folders 
# 1. expected_output_tests
# 2. input_tests
# pass in inputs to the jar file and compare the output to the expected output
# if the output is the same, then the test passes
# if the output is different, then the test fails

import os
import subprocess
import sys

# get the path to the jar file
jar_path = sys.argv[1]

# get the path to the input tests
input_tests_path = sys.argv[2]

# get the path to the expected output tests
expected_output_tests_path = sys.argv[3]

# get the list of input tests
input_tests = os.listdir(input_tests_path)

# get the list of expected output tests
expected_output_tests = os.listdir(expected_output_tests_path)

# sort the input tests
input_tests.sort()

# sort the expected output tests
expected_output_tests.sort()

# loop through the input tests
for i in range(len(input_tests)):
    # get the input test
    input_test = input_tests[i]

    # get the expected output test
    expected_output_test = expected_output_tests[i]

    # get the path to the input test
    input_test_path = os.path.join(input_tests_path, input_test)

    # get the path to the expected output test
    expected_output_test_path = os.path.join(expected_output_tests_path, expected_output_test)

    # open the input test
    with open(input_test_path, 'r') as input_test_file:
        # open the expected output test
        with open(expected_output_test_path, 'r') as expected_output_test_file:
            # get the expected output
            expected_output = expected_output_test_file.read()

            # execute the jar file
            output = subprocess.run(['java', '-jar', jar_path], stdin=input_test_file, stdout=subprocess.PIPE)

            # get the output
            output = output.stdout.decode('utf-8')

            # check if the output is the same as the expected output
            if output == expected_output:
                # print the input test
                print(input_test)

                # print the expected output test
                print(expected_output_test)

                # print the output
                print(output)

                # print the expected output
                print(expected_output)

                # print the test passed
                print('Test passed')

                # print a new line
                print()

            else:
                # print the input test
                print(input_test)

                # print the expected output test
                print(expected_output_test)

                # print the output
                print(output)

                # print the expected output
                print(expected_output)

                # print the test failed
                print('Test failed')

                # print a new line
                print()