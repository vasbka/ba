class ExpertsController < ApplicationController
  def index
    @experts = Expert.all
    respond_to  do |format|
      format.html {render 'index'}
      format.json {render plain: @experts.to_json}
    end
  end

  def show
    @expert = Expert.find(params[:id])
  end

  def new
    @expert = Expert.new
    @procedures = Procedure.all
  end

  def edit
    @expert = Expert.find(params[:id])
    @procedures = Procedure.all
  end

  def create
    @expert = Expert.new(expert_params)

    @expert.save

    uploaded_io = params[:expert][:picture]
    File.open(Rails.root.join(
        ApplicationController::FILES_ROOT_FOLDER,
        ApplicationController::PICTURE_FOLDER + "/experts",
        @expert[:id].to_s + ApplicationController::DEFAULT_PICTURE_FORMAT), 'wb') do |file|
      file.write(uploaded_io.read)
    end

    saveExpProcedures
    saveWorkingDays


    if @expert.save
      redirect_to @expert
    else
      render 'new'
    end
  end

  def update
    @expert = Expert.find(params[:id])
    ExpProcedure.where(:expert_id => @expert.id).delete_all
    ExpertDay.where(:expert_id => @expert.id).delete_all

    saveExpProcedures
    saveWorkingDays

    if @expert.update(expert_params)
      redirect_to @expert
    else
      render 'edit'
    end
  end

  def destroy
    @expert = Expert.find(params[:id])
    @expert.destroy

    redirect_to experts_path
  end


  def get_by_phone
    respond_to  do |format|
        format.json { render :json =>
          Expert.where(:phoneNumber => params[:phone_number])
          .first
          .time_slots
          .select { |timeSlot| timeSlot.date == Date.today }
          .uniq
          .to_json(:only => [:startTime, :endTime])
        }
    end
  end


  def get_possible_time_slots
    expertId = params[:expert_id]
    procedures = params[:procedures]
    expProcedures = ExpProcedure.where(:expert_id => expertId, :procedure_id => procedures).all
    totalTime = 0
    expProcedures.each do |expProcedure|
      totalTime += expProcedure.time_consume.to_i
   end
   possibleTime = []
   requestDay = params[:day_number].to_i
   expert = Expert.where(:id => params[:expert_id]).first
   day = expert.expert_days.where(:dayNumber => requestDay).first
   startTime = day[:startTime].to_time
   endTime = day[:endTime].to_time
   timeSlots = expert.time_slots.where(:date => DateTime.now.change(day: requestDay).to_date).all

   begin
     timeSlots.all? { |timeSlot| (startTime + totalTime.minutes) < timeSlot.startTime || startTime > timeSlot.endTime } &&
        possibleTime.push(startTime.strftime('%H:%M'))
   end while ((startTime += 1800) + totalTime.minutes) < endTime

   respond_to  do |format|
       format.json { render :json =>
         possibleTime.to_json
       }
   end
  end

  def get_working_days
    respond_to  do |format|
        format.json { render :json =>
          Expert.where(:id => params[:expert_id])
          .first
          .expert_days
          .to_json
        }
    end
  end

  private
    def expert_params
      params.require(:expert).permit(:firstName, :lastName,
        :lastName, :secondName, :description, :phoneNumber)
    end

    def procedure_params
      params.require(:procedure)
    end

    def days_params
      params.require(:expert).permit(:days, :startTime, :endTime)
    end

    def saveExpProcedures
      @expProcedure = {}
      @expProcedure[:expert_id] = @expert[:id]
      procedure_params.each do |procedure|
        if procedure[1][:price].present? then
          @expProcedure[:procedure_id] = procedure[1][:id]
          @expProcedure[:price] = procedure[1][:price]
          @expProcedure[:time_consume] = procedure[1][:time]
          ExpProcedure.new(@expProcedure).save
        end
      end
    end

    def saveWorkingDays
     @workingDay = {}
     @workingDay[:expert_id] = @expert[:id]
     days_params[:days].split(',').each do |workingDay|
       if workingDay.include?('-')
         startDate = workingDay.split('-')[0].to_i
         endDate = workingDay.split('-')[1].to_i
         until startDate > endDate do
           @workingDay[:dayNumber] = startDate
           @workingDay[:startTime] = days_params[:startTime]
           @workingDay[:endTime] = days_params[:endTime]
           ExpertDay.new(@workingDay).save
           startDate = startDate.to_i + 1
         end
       elsif
         @workingDay[:dayNumber] = workingDay
         @workingDay[:startTime] = days_params[:startTime]
         @workingDay[:endTime] = days_params[:endTime]
         ExpertDay.new(@workingDay).save
       end
     end

     def time_iterate(start_time, end_time, step, &block)
        begin
          yield(start_time)
        end while (start_time += step) <= end_time
      end
    end
end
