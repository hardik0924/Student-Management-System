package net.hardik.sms.service.implementation;
import lombok.AllArgsConstructor;
import net.hardik.sms.dto.StudentDto;
import net.hardik.sms.entity.Department;
import net.hardik.sms.entity.Student;
import net.hardik.sms.exception.ResourceNotFoundException;
import net.hardik.sms.mapper.StudentMapper;
import net.hardik.sms.repository.DepartmentRepository;
import net.hardik.sms.repository.StudentRepository;
import net.hardik.sms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImplementation implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        Student student = StudentMapper.mapToStudent(studentDto);

        Department department = departmentRepository.findById(studentDto.getDepartmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Department was not found with id: "
                                    + studentDto.getDepartmentId()));
        student.setDepartment(department);
        Student savedStudent =  studentRepository.save(student);
        return StudentMapper.mapToStudentDto(savedStudent);
    }

    @Override
    public StudentDto getStudentById(Long studentId) {
       Student student = studentRepository.findById(studentId).orElseThrow(()->
                new ResourceNotFoundException("Student was not found with given id: " + studentId));
        return StudentMapper.mapToStudentDto(student);
    }

    @Override
    public List<StudentDto> getAllStudents() {
       List<Student> studentList =  studentRepository.findAll();
        return studentList.stream()
                .map(StudentMapper::mapToStudentDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto updateStudent(Long studentId, StudentDto studentDto) {
        Student student = studentRepository.findById(studentId).orElseThrow(()->
                new ResourceNotFoundException("Student was not found with given id: " + studentId));

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setEmail(studentDto.getEmail());

        Department department = departmentRepository.findById(studentDto.getDepartmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Department was not found with id: "
                        + studentDto.getDepartmentId()));
        student.setDepartment(department);

        Student savedStudent = studentRepository.save(student);
        return StudentMapper.mapToStudentDto(savedStudent);
    }

    @Override
    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(()->
                new ResourceNotFoundException("Student was not found with given id: " + studentId));
        studentRepository.deleteById(studentId);
    }
}
