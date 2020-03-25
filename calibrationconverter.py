import sys, getopt
import pandas


def to_java_string(input: dict) -> str:
    return "shooterCalibrationMap.put(new InterpolatingDouble({}), new InterpolatingDouble({}));\n".format(float(input[0]), float(input[1]))


def main(argv):
    inputfile = 'CalibrationData.csv'
    outputfile = ''
    
    # Parse command line arguments
    try:
        opts, args = getopt.getopt(argv,"ho:",["ofile=",])
    except getopt.GetoptError:
        print('Usage: test.py -o <outputfile>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('Usage: test.py -o <outputfile>')
            sys.exit()
        elif opt in ("-o", "--ofile"):
            outputfile = arg
    if outputfile is '':
        print('Usage: test.py -o <outputfile>')
        sys.exit(2)
		
    with open(inputfile) as csvfile:
        reader = pandas.read_csv(csvfile, header=0, names=['distance', 'speed', 'real distance'])
        with open(outputfile, 'w') as output:        
            for row in reader.itertuples(index=False, name='CalibrationPoint'):
                output.writelines(to_java_string(row))
                # print(row)
    

if __name__ == "__main__":
   main(sys.argv[1:])